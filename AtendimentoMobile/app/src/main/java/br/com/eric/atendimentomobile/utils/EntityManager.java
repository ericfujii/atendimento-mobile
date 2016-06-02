package br.com.eric.atendimentomobile.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.eric.atendimentomobile.utils.annotations.Column;
import br.com.eric.atendimentomobile.utils.annotations.DateFormat;
import br.com.eric.atendimentomobile.utils.annotations.EmbeddedId;
import br.com.eric.atendimentomobile.utils.annotations.Id;
import br.com.eric.atendimentomobile.utils.annotations.JoinColumn;
import br.com.eric.atendimentomobile.utils.annotations.JoinColumns;
import br.com.eric.atendimentomobile.utils.annotations.Table;
import br.com.eric.atendimentomobile.utils.annotations.Transient;

/**
 * Created by eric on 11/02/15.
 * Classe de acesso a base de Dados SQLite
 */
public class EntityManager {
    private DatabaseHelper helper;
    private Context context;

    public EntityManager(Context context) {
        helper = new DatabaseHelper(context);
        this.context = context;
    }

    public <T> T save(T object) throws DataBaseException {
        try {
            SQLiteDatabase db = helper.getWritableDatabase();

            String tableName = getTableName(object.getClass());

            ContentValues values = loadContentValues(object);

            long rowid = db.insert(tableName, null, values);
            T objectReturn = (T) getByWhere(object.getClass(), "rowid = "+rowid, null).get(0);
            return objectReturn;
        } catch (Exception e) {
            throw new DataBaseException(e.getMessage());
        }
    }

    public <T> List<T> select(Class<T> objectsClass, String query) throws DataBaseException {
        try {
            List<T> tList = new ArrayList<>();
            SQLiteDatabase db = helper.getReadableDatabase();
            Cursor cursor = db.rawQuery(query, null);
            while (cursor.moveToNext()) {
                T object = objectsClass.newInstance();
                for (Field field : objectsClass.getDeclaredFields()) {
                    Transient aTransient = field.getAnnotation(Transient.class);
                    if (aTransient == null) {
                        field.setAccessible(true);
                        EmbeddedId embeddedId = field.getAnnotation(EmbeddedId.class);
                        if (embeddedId != null) {
                            Object newObject = field.getType().newInstance();
                            Field[] idFields = newObject.getClass().getDeclaredFields();
                            for (Field idField : idFields) {
                                idField.setAccessible(true);
                                setSelectValue(idField, newObject, cursor, null);
                            }
                            field.set(object, newObject);
                        } else {
                            JoinColumns joinColumns = field.getAnnotation(JoinColumns.class);
                            if (joinColumns != null) {
                                Object newObject = field.getType().newInstance();
                                Field compositeIdField = getIdField(field.getType());
                                Object compositeIdObject = compositeIdField.getType().newInstance();
                                for (JoinColumn join : joinColumns.value()) {
                                    Field joinField = getFieldByColumn(join.referencedColumnName(), compositeIdField.getType());
                                    setSelectValue(joinField, compositeIdObject, cursor, join.name());
                                }
                                compositeIdField.set(newObject, compositeIdObject);
                                field.set(object, newObject);
                            } else {
                                setSelectValue(field, object, cursor, null);
                            }
                        }
                    }
                }
                tList.add(object);
            }
            return tList;
        } catch (Exception e) {
            throw new DataBaseException(e.getMessage());
        }
    }

    public void executeNativeQuery(String sql) throws DataBaseException {
        try {
            SQLiteDatabase db = helper.getReadableDatabase();
            db.execSQL(sql);
        } catch (Exception e) {
            throw new DataBaseException(e.getMessage());
        }
    }

    private <T> void setSelectValue(Field field, T object, Cursor cursor, String fkColumn) throws IllegalAccessException, ParseException, InstantiationException, DataBaseException {
        if (fkColumn == null) {
            fkColumn = getFieldColumnName(field);
        }
        int columnIndex = cursor.getColumnIndex(fkColumn);
        if (columnIndex == -1) {
            field.set(object, null);
        } else if (cursor.getString(columnIndex) == null) {
            field.set(object, null);
        } else if (field.getType().equals(String.class)) {
            field.set(object, cursor.getString(columnIndex));
        } else if (field.getType().equals(Integer.class)) {
            field.set(object, cursor.getInt(columnIndex));
        } else if (field.getType().equals(Long.class)) {
            field.set(object, cursor.getLong(columnIndex));
        } else if (field.getType().equals(Double.class)) {
            field.set(object, cursor.getDouble(columnIndex));
        } else if (field.getType().equals(BigDecimal.class)) {
            field.set(object, new BigDecimal(cursor.getString(columnIndex)));
        } else if (field.getType().equals(Float.class)) {
            field.set(object, cursor.getFloat(columnIndex));
        } else if (field.getType().equals(Boolean.class)) {
            field.set(object, cursor.getString(columnIndex).equals("true"));
        } else if (field.getType().equals(Calendar.class)) {
            DateFormat dateFormat = field.getAnnotation(DateFormat.class);
            String formato;
            if (dateFormat != null) {
                formato = dateFormat.format();
            } else {
                formato = "dd-MM-yyyy HH:mm:ss";
            }
            SimpleDateFormat sdf = new SimpleDateFormat(formato);
            Calendar calendar = Calendar.getInstance();
            String stringValue = cursor.getString(columnIndex);
            if (stringValue != null) {
                calendar.setTimeInMillis(sdf.parse(stringValue).getTime());
                field.set(object, calendar);
            }
        } else if (field.getType().isEnum()) {
            field.set(object, getEnum(field.getType(), cursor.getString(columnIndex)));
        } else {
            Object newObject = field.getType().newInstance();
            Field idField = getIdField(newObject.getClass());
            setSelectValue(idField, newObject, cursor, getFieldColumnName(field));
            field.set(object, newObject);
        }
    }

    private String getFieldColumnName(Field field) {
        String fieldColumnName;
        Column column = field.getAnnotation(Column.class);
        if (column != null) {
            fieldColumnName = column.name();
        } else {
            JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);
            if (joinColumn != null) {
                fieldColumnName = joinColumn.name();
            } else {
                fieldColumnName = field.getName().toLowerCase();
            }
        }
        return fieldColumnName;
    }

    private <T> void addFieldValue(ContentValues values, String columnName, Field field, T object) throws IllegalAccessException {
        if (object != null) {
            if(field.get(object) != null) {
                if (field.getType().equals(String.class)) {
                    values.put(columnName, (String) field.get(object));
                } else if (field.getType().equals(Integer.class)) {
                    values.put(columnName, (Integer) field.get(object));
                } else if (field.getType().equals(Long.class)) {
                    values.put(columnName, (Long) field.get(object));
                } else if (field.getType().equals(Double.class)) {
                    values.put(columnName, (Double) field.get(object));
                } else if (field.getType().equals(BigDecimal.class)) {
                    values.put(columnName, ((BigDecimal)field.get(object)).toString());
                } else if (field.getType().equals(Float.class)) {
                    values.put(columnName, (Float) field.get(object));
                } else if (field.getType().equals(Boolean.class)) {
                    values.put(columnName, (Boolean) field.get(object));
                } else if (field.getType().equals(Calendar.class)) {
                    DateFormat dateFormat = field.getAnnotation(DateFormat.class);
                    String formato;
                    if (dateFormat != null) {
                        formato = dateFormat.format();
                    } else {
                        formato = "dd-MM-yyyy HH:mm:ss";
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat(formato);
                    Calendar calendar = (Calendar) field.get(object);
                    if (calendar != null) {
                        values.put(columnName, sdf.format(calendar.getTime()));
                    }
                } else if (field.getType().isEnum()) {
                    values.put(columnName, field.get(object).toString());
                }
            }
        }
    }

    private <T> Field getIdField(Class<T> objectClass) throws DataBaseException {
        for(Field field : objectClass.getDeclaredFields()){
            field.setAccessible(true);
            Id id = field.getAnnotation(Id.class);
            EmbeddedId embeddedId = field.getAnnotation(EmbeddedId.class);
            if(id != null || embeddedId != null){
                return field;
            }
        }

        throw new DataBaseException(objectClass.getName() + " has no id column");

    }

    private <T> String getTableName(Class<T> objectClass) {
        String tableName;
        Table table = objectClass.getAnnotation(Table.class);
        if (table != null) {
            tableName = table.name();
        } else {
            tableName = objectClass.getSimpleName().toLowerCase();
        }
        return tableName;
    }

    private <T> T getEnum(Class<T> enumType, String value) {
        for (T c : enumType.getEnumConstants()) {
            if (c.toString().equals(value)) {
                return c;
            }
        }
        return null;
    }

    public void dropBasicTables() {
        dropTable("midia");
        dropTable("motivo_tabulacao");
        dropTable("motivo_tabulacao_tipo");
        dropTable("motivo_migracao");
        dropTable("concorrente");
        dropTable("estado");
        dropTable("cidade");
        dropTable("estado_civil");
        dropTable("escolaridade");
        dropTable("motivo_nao_venda");
        dropTable("produto_tipo");
        dropTable("produto");
        dropTable("tipo_contrato");
        dropTable("periodo_instalacao");
        dropTable("combinacao_produto_tipo");
        dropTable("combinacao_produto_tipo_produto_tipo");
        dropTable("tipo_contrato_combinacao_produto_tipo");
        dropTable("ocorrencia");
        dropTable("tipo_ponto_adicional");
        dropTable("operadora_telefonia");
        dropTable("tipo_compartilhamento");
        dropTable("tamanho_chip");
        dropTable("detalhamento");
        dropTable("bandeira_sistema");
        dropTable("bandeira_sistema_produto");
        dropTable("banco");
        dropTable("condominio_contato_cargo");
        dropTable("condominio_operacao_comercial");
        dropTable("condominio_plantao_situacao");
        dropTable("condominio_status_infra");
        dropTable("vencimento_fatura");
        dropTable("tecnologia_disponivel");
        dropTable("ocorrencia");
        dropTable("detalhamento");
        dropTable("mobile_permissao");
    }

    public void createBasicTables() {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS versao_pacote (id INTEGER PRIMARY KEY, " +
                "versao NUMERIC, data_cadastro TEXT, data_atualizacao TEXT, descricao TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS midia (id INTEGER PRIMARY KEY, " +
                "descricao TEXT, situacao TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS motivo_tabulacao_tipo (id INTEGER PRIMARY KEY, " +
                "descricao TEXT, situacao TEXT, icone TEXT, retroalimentavel TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS motivo_tabulacao (id INTEGER PRIMARY KEY, " +
                "descricao TEXT, situacao TEXT, deve_baixar_hp TEXT, " +
                "_motivo_tabulacao_tipo INTEGER, concorrencia TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS motivo_migracao (id INTEGER PRIMARY KEY, " +
                "descricao TEXT, situacao TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS concorrente (id INTEGER PRIMARY KEY, " +
                "descricao TEXT, _produto_tipo INTEGER, situacao TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS estado (uf TEXT PRIMARY KEY, " +
                "nome TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS cidade (id INTEGER PRIMARY KEY, " +
                "nome TEXT, codigo TEXT, sigla TEXT, _estado TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS estado_civil (id INTEGER PRIMARY KEY, " +
                "descricao TEXT, situacao TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS escolaridade (id INTEGER PRIMARY KEY, " +
                "descricao TEXT, situacao TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS motivo_nao_venda (id INTEGER PRIMARY KEY, " +
                "descricao TEXT, situacao TEXT, _produto_tipo INTEGER)");

        db.execSQL("CREATE TABLE IF NOT EXISTS produto_tipo (" +
                "id INTEGER PRIMARY KEY, " +
                "descricao TEXT," +
                "situacao TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS produto (" +
                "id INTEGER PRIMARY KEY, " +
                "descricao TEXT, " +
                "_produto_pai INTEGER, " +
                "_produto_tipo INTEGER, " +
                "situacao TEXT, " +
                "sistema TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS tipo_contrato (" +
                "id INTEGER PRIMARY KEY, " +
                "descricao TEXT," +
                "situacao TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS periodo_instalacao (" +
                "id INTEGER PRIMARY KEY, " +
                "descricao TEXT," +
                "situacao TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS combinacao_produto_tipo (" +
                "id INTEGER PRIMARY KEY, " +
                "descricao TEXT," +
                "situacao TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS combinacao_produto_tipo_produto_tipo ( " +
                "_combinacao_produto_tipo INTEGER, " +
                "_produto_tipo INTEGER, " +
                "PRIMARY KEY(_combinacao_produto_tipo, _produto_tipo))");

        db.execSQL("CREATE TABLE IF NOT EXISTS tipo_contrato_combinacao_produto_tipo (" +
                "_combinacao_produto_tipo INTEGER, " +
                "_tipo_contrato INTEGER, " +
                "PRIMARY KEY (_combinacao_produto_tipo, _tipo_contrato))");

        db.execSQL("CREATE TABLE IF NOT EXISTS ocorrencia (" +
                "id INTEGER PRIMARY KEY, " +
                "descricao TEXT, " +
                "codigo TEXT, " +
                "cor TEXT, " +
                "sequencia INTEGER, " +
                "situacao TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS tipo_ponto_adicional (" +
                "id INTEGER PRIMARY KEY, " +
                "descricao TEXT, " +
                "situacao TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS operadora_telefonia (" +
                "id INTEGER PRIMARY KEY, " +
                "descricao TEXT, " +
                "situacao TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS tipo_compartilhamento (" +
                "id INTEGER PRIMARY KEY, " +
                "descricao TEXT, " +
                "situacao TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS tamanho_chip (" +
                "id INTEGER PRIMARY KEY, " +
                "descricao TEXT, " +
                "situacao TEXT," +
                "modelo_aparelho TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS detalhamento (" +
                "id INTEGER PRIMARY KEY, " +
                "descricao TEXT, " +
                "codigo TEXT, " +
                "_ocorrencia INTEGER," +
                "situacao TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS bandeira_sistema (" +
                "id INTEGER PRIMARY KEY, " +
                "descricao TEXT, " +
                "icone TEXT," +
                "situacao TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS bandeira_sistema_produto (" +
                "_bandeira_sistema INTEGER, " +
                "_produto INTEGER, " +
                " PRIMARY KEY (_bandeira_sistema, _produto))");

        db.execSQL("CREATE TABLE IF NOT EXISTS banco (" +
                "id INTEGER PRIMARY KEY, " +
                "descricao TEXT," +
                "situacao TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS condominio_contato_cargo (" +
                "id INTEGER, " +
                "  descricao TEXT, " +
                "  situacao TEXT) ");

        db.execSQL("CREATE TABLE IF NOT EXISTS condominio_operacao_comercial (" +
                "id INTEGER, " +
                "  descricao TEXT, " +
                "  situacao TEXT) ");

        db.execSQL("CREATE TABLE IF NOT EXISTS condominio_plantao_situacao (" +
                "id INTEGER, " +
                "  descricao TEXT, " +
                "  situacao TEXT) ");

        db.execSQL("CREATE TABLE IF NOT EXISTS condominio_status_infra (" +
                "id INTEGER, " +
                "  descricao TEXT, " +
                "  situacao TEXT) ");

        db.execSQL("CREATE TABLE IF NOT EXISTS vencimento_fatura (" +
                "vencimento INTEGER PRIMARY KEY, " +
                "situacao TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS tecnologia_disponivel (" +
                "id INTEGER PRIMARY KEY, " +
                "situacao TEXT,"+
                "descricao TEXT,"+
                "_produto_tipo INTEGER)");

        db.execSQL("CREATE TABLE IF NOT EXISTS ocorrencia (" +
                "id INTEGER PRIMARY KEY, " +
                "descricao TEXT, " +
                "codigo TEXT, " +
                "cor TEXT, " +
                "sequencia INTEGER, " +
                "situacao TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS detalhamento (" +
                "id INTEGER PRIMARY KEY, " +
                "descricao TEXT, " +
                "codigo TEXT, " +
                "_ocorrencia INTEGER," +
                "situacao TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS mobile_permissao (" +
                "id INTEGER PRIMARY KEY, " +
                " descricao TEXT , " +
                " icone TEXT , " +
                " label TEXT, " +
                " segue TEXT , " +
                " activity TEXT, " +
                " sequencia INTEGER, " +
                " situacao TEXT, " +
                " lado_menu TEXT) ");

    }

    public void dropTable(String tableName) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
    }

    public <T> boolean delete(T object) throws DataBaseException {
        try {
            SQLiteDatabase db = helper.getWritableDatabase();
            String tableName = getTableName(object.getClass());
            Field idField = getIdField(object.getClass());
            StringBuilder whereQuery = new StringBuilder();
            String[] whereParameters = null;

            EmbeddedId embeddedId = idField.getAnnotation(EmbeddedId.class);
            if (embeddedId != null) {
                Field[] idFields = idField.getType().getDeclaredFields();
                int i = 0;
                int validFields = 0;
                for (Field field : idFields) {
                    field.setAccessible(true);
                    if (!field.getName().contains("$")) {
                        validFields++;
                    }
                }
                for (Field field : idFields) {
                    field.setAccessible(true);
                    if (!field.getName().contains("$")) {
                        //whereParameters[i-1] = field.get(idField.get(object)).toString();
                        whereQuery.append(generateWhere(field, field.get(idField.get(object))));
                        //whereQuery.append(getFieldColumnName(field));
                        //whereQuery.append(" = ?");
                        if (i < validFields) {
                            whereQuery.append(" AND ");
                        }
                        i++;
                    }
                }
            } else {
                whereParameters = new String[1];
                whereParameters[0] = idField.get(object).toString();
                whereQuery.append(getFieldColumnName(idField));
                whereQuery.append(" = ?");
            }

            long result = db.delete(tableName, whereQuery.toString(), whereParameters);
            return result == 1;
        } catch (Exception e) {
            throw new DataBaseException(e.getMessage());
        }
    }

    public <T> boolean atualizar(T object) throws DataBaseException {
        try {
            SQLiteDatabase db = helper.getWritableDatabase();
            String tableName = getTableName(object.getClass());
            Field idField = getIdField(object.getClass());
            StringBuilder whereQuery = new StringBuilder();
            String[] whereParameters;

            ContentValues values = loadContentValues(object);

            EmbeddedId embeddedId = idField.getAnnotation(EmbeddedId.class);
            if (embeddedId != null) {
                Field[] idFields = idField.getClass().getDeclaredFields();
                whereParameters = new String[idFields.length];
                int i = 1;
                for (Field field : idFields) {
                    field.setAccessible(true);
                    whereParameters[i-1] = field.get(idField.get(object)).toString();
                    whereQuery.append(getFieldColumnName(field));
                    whereQuery.append(" = ?");
                    if (i < idFields.length) {
                        whereQuery.append(" AND ");
                    }
                    i++;
                }
            } else {
                whereParameters = new String[1];
                whereParameters[0] = idField.get(object).toString();
                whereQuery.append(getFieldColumnName(idField));
                whereQuery.append(" = ?");
            }

            long result = db.update(tableName, values, whereQuery.toString(), whereParameters);
            return result == 1;
        } catch (Exception e) {
            throw new DataBaseException(e.getMessage());
        }
    }

    private <T> ContentValues loadContentValues (T object) throws IllegalAccessException, DataBaseException {
        ContentValues values = new ContentValues();

        for (Field field : object.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Transient aTransient = field.getAnnotation(Transient.class);
            if (aTransient == null) {
                Column column = field.getAnnotation(Column.class);
                if (column != null) {
                    addFieldValue(values, column.name(), field, object);
                } else {
                    JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);
                    if (joinColumn != null) {
                        Field idFieldJoin = getIdField(field.getType());
                        addFieldValue(values, joinColumn.name(), idFieldJoin, field.get(object));
                    } else {
                        EmbeddedId embeddedId = field.getAnnotation(EmbeddedId.class);
                        if (embeddedId != null) {
                            for (Field embeddedIdField : field.getType().getDeclaredFields()) {
                                embeddedIdField.setAccessible(true);
                                addFieldValue(values, getFieldColumnName(embeddedIdField), embeddedIdField, field.get(object));
                            }
                        } else {
                            JoinColumns joinColumns = field.getAnnotation(JoinColumns.class);
                            if (joinColumns != null) {
                                for (JoinColumn join : joinColumns.value()) {
                                    Field joinField = getFieldByColumn(join.referencedColumnName(), field.getType());
                                    addFieldValue(values, join.name(), joinField, field.get(object));
                                }
                            } else {
                                addFieldValue(values, field.getName().toLowerCase(), field, object);
                            }
                        }
                    }
                }
            }
        }

        return values;
    }

    private <T> Field getFieldByColumn(String columnName, Class<T> objectClass) {
        for (Field field : objectClass.getDeclaredFields()) {
            field.setAccessible(true);
            Column column = field.getAnnotation(Column.class);
            if (column != null) {
                if (column.name().equals(columnName)) {
                    return field;
                }
            } else {
                if (columnName.equals(field.getName().toLowerCase())) {
                    return field;
                }
            }
        }
        return null;
    }

    public <T> void initialize(T object) throws DataBaseException {
        try {
            if (object != null) {
                String query = generateObjectQuery(object.getClass());
                Field idFieldWhere = getIdField(object.getClass());

                StringBuilder whereQuery = new StringBuilder(query);
                whereQuery.append(" WHERE ");
                String[] whereParameters;

                EmbeddedId embeddedIdWhere = idFieldWhere.getAnnotation(EmbeddedId.class);
                if (embeddedIdWhere != null) {
                    Field[] idFields = idFieldWhere.getClass().getDeclaredFields();
                    whereParameters = new String[idFields.length];
                    int i = 1;
                    for (Field field : idFields) {
                        field.setAccessible(true);
                        whereParameters[i - 1] = field.get(idFieldWhere.get(object)).toString();
                        whereQuery.append(getFieldColumnName(field));
                        whereQuery.append(" = ?");
                        if (i < idFields.length) {
                            whereQuery.append(" AND ");
                        }
                        i++;
                    }
                } else {
                    whereParameters = new String[1];
                    whereParameters[0] = idFieldWhere.get(object).toString();
                    whereQuery.append(getFieldColumnName(idFieldWhere));
                    whereQuery.append(" = ?");
                }

                SQLiteDatabase db = helper.getReadableDatabase();
                Cursor cursor = db.rawQuery(whereQuery.toString(), whereParameters);
                while (cursor.moveToNext()) {
                    for (Field field : object.getClass().getDeclaredFields()) {
                        field.setAccessible(true);
                        EmbeddedId embeddedId = field.getAnnotation(EmbeddedId.class);
                        if (embeddedId != null) {
                            Object newObject = field.getType().newInstance();
                            Field[] idFields = newObject.getClass().getDeclaredFields();
                            for (Field idField : idFields) {
                                idField.setAccessible(true);
                                setSelectValue(idField, newObject, cursor, null);
                            }
                            field.set(object, newObject);
                        } else {
                            JoinColumns joinColumns = field.getAnnotation(JoinColumns.class);
                            if (joinColumns != null) {
                                Object newObject = field.getType().newInstance();
                                Field compositeIdField = getIdField(field.getType());
                                Object compositeIdObject = compositeIdField.getType().newInstance();
                                for (JoinColumn join : joinColumns.value()) {
                                    Field joinField = getFieldByColumn(join.referencedColumnName(), compositeIdField.getType());
                                    setSelectValue(joinField, compositeIdObject, cursor, join.name());
                                }
                                compositeIdField.set(newObject, compositeIdObject);
                                field.set(object, newObject);
                            } else {
                                setSelectValue(field, object, cursor, null);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new DataBaseException(e.getMessage());
        }
    }

    private <T> String generateObjectQuery(Class<T> objectClass) throws DataBaseException {
        try {
            Field[] fields = objectClass.getDeclaredFields();
            int i = 1;
            int notTransients = 0;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("SELECT rowid, ");
            for (Field field : fields) {
                Transient aTransient = field.getAnnotation(Transient.class);
                if (aTransient == null && !field.getName().contains("$")) {
                    notTransients++;
                }
            }

            for (Field field : fields) {
                Transient aTransient = field.getAnnotation(Transient.class);
                if (aTransient == null && !field.getName().contains("$")) {
                    EmbeddedId embeddedId = field.getAnnotation(EmbeddedId.class);
                    if (embeddedId != null) {
                        Field[] idFields = field.getType().getDeclaredFields();
                        int j = 1;
                        int validFields = 0;
                        for (Field idField : idFields) {
                            if (!idField.getName().contains("$")) {
                                validFields++;
                            }
                        }
                        for (Field idField : idFields) {
                            if (!idField.getName().contains("$")) {
                                stringBuilder.append(getFieldColumnName(idField));
                                if (j != validFields) {
                                    stringBuilder.append(", ");
                                }
                                j++;
                            }
                        }
                    } else {
                        JoinColumns joinColumns = field.getAnnotation(JoinColumns.class);
                        if (joinColumns != null) {
                            int k = 1;
                            for (JoinColumn join : joinColumns.value()) {
                                stringBuilder.append(join.name());
                                if (k != joinColumns.value().length) {
                                    stringBuilder.append(", ");
                                }
                                k++;
                            }
                        } else {
                            String fieldColumnName = getFieldColumnName(field);
                            stringBuilder.append(fieldColumnName);
                        }
                    }
                    if (i != notTransients) {
                        stringBuilder.append(", ");
                    } else {
                        stringBuilder.append(" ");
                    }
                    i++;
                }
            }
            stringBuilder.append("FROM ");
            String tableName = getTableName(objectClass);
            stringBuilder.append(tableName);
            Log.d("SQL genObjectQuery ", stringBuilder.toString());
            return stringBuilder.toString();
        } catch (Exception e) {
            throw new DataBaseException(e.getMessage());
        }
    }

    public <T> List<T> getAll(Class<T> objectsClass) throws DataBaseException {
        return select(objectsClass, generateObjectQuery(objectsClass));
    }

    public <T> List<T> getByWhere(Class<T> objectsClass, String where, String orderBy) throws DataBaseException {
        StringBuilder stringBuilder = new StringBuilder(generateObjectQuery(objectsClass));
        if (where != null) {
            stringBuilder.append(" WHERE ");
            stringBuilder.append(where);
        }
        if (orderBy != null) {
            stringBuilder.append(" ORDER BY ");
            stringBuilder.append(orderBy);
        }
        Log.d("SQL getByWhere", stringBuilder.toString());
        return  select(objectsClass, stringBuilder.toString());
    }

    public <T> T getById(Class<T> objectClass, Object objectId) throws DataBaseException {
        try {
            Field idField = getIdField(objectClass);
            idField.setAccessible(true);
            if (objectId.getClass().equals(idField.getType())) {
                StringBuilder queryWhere = new StringBuilder();
                EmbeddedId embeddedId = idField.getAnnotation(EmbeddedId.class);

                if (embeddedId == null) {
                    queryWhere.append(generateWhere(idField, objectId));
                } else {
                    Field[] embeddedIdFields = objectId.getClass().getDeclaredFields();
                    int i = 0;
                    int validFields = 0;
                    for (Field embeddedIdField : embeddedIdFields) {
                        if (!embeddedIdField.getName().contains("$")) {
                            validFields++;
                        }
                    }
                    for (Field embeddedIdField : embeddedIdFields) {
                        if (!embeddedIdField.getName().contains("$")) {
                            embeddedIdField.setAccessible(true);
                            queryWhere.append(generateWhere(embeddedIdField, embeddedIdField.get(objectId)));
                            if (i != validFields) {
                                queryWhere.append(" AND ");
                            }
                        }
                        i++;
                    }
                }

                List<T> list = getByWhere(objectClass, queryWhere.toString(), null);
                if (list != null && list.size() > 0) {
                    return list.get(0);
                } else {
                    throw new DataBaseException("Id nao encontrado");
                }
            } else {
                throw new DataBaseException("Objeto de classe diferente do id");
            }
        } catch (Exception e) {
            throw new DataBaseException(e.getMessage());
        }
    }

    private <T> String generateWhere(Field field, Object object) {
        StringBuilder queryWhere = new StringBuilder();
        queryWhere.append(getFieldColumnName(field));
        queryWhere.append(" = ");
        if (field.getType().equals(String.class)) {
            queryWhere.append("'");
            queryWhere.append((String) object);
            queryWhere.append("'");
        } else if (field.getType().equals(Integer.class)) {
            queryWhere.append((Integer) object);
        } else if (field.getType().equals(Long.class)) {
            queryWhere.append((Long) object);
        } else if (field.getType().equals(Double.class)) {
            queryWhere.append((Double) object);
        } else if (field.getType().equals(BigDecimal.class)) {
            queryWhere.append(((BigDecimal) object).toString());
        } else if (field.getType().equals(Float.class)) {
            queryWhere.append((Float) object);
        } else if (field.getType().equals(Boolean.class)) {
            queryWhere.append((Boolean) object);
        } else if (field.getType().equals(Calendar.class)) {
            queryWhere.append("'");
            SimpleDateFormat sdf = new SimpleDateFormat(
                    "dd-MM-yyyy HH:mm:ss");
            Calendar calendar = (Calendar) object;
            if (calendar != null) {
                queryWhere.append(sdf.format(calendar.getTime()));
            }
            queryWhere.append("'");
        } else if (field.getType().isEnum()) {
            queryWhere.append("'");
            queryWhere.append(object.toString());
            queryWhere.append("'");
        }

        return queryWhere.toString();
    }

    public boolean resetDatabase() {
        File file = new File("/data/data/multisales.mobile.nx.com.br.multisalesmobile/databases/multisales");
        boolean deleted = file.delete();
        return deleted;
    }


}
