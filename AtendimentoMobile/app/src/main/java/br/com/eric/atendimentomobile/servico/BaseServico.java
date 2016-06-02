package br.com.eric.atendimentomobile.servico;

import android.content.Context;

import java.util.List;

import br.com.eric.atendimentomobile.utils.DataBaseException;
import br.com.eric.atendimentomobile.utils.EntityManager;

/**
 * Created by eric on 01-06-2015.
 */
public abstract class BaseServico<T> {

    final Class<T> tClass;

    private EntityManager entityManager;

    public T salvar(T object) throws DataBaseException {
        return entityManager.save(object);
    }

    public boolean atualizar(T object) throws DataBaseException {
        return entityManager.atualizar(object);
    }

    public List<T> obterTodos() throws DataBaseException {
        return entityManager.getAll(tClass);
    }

    public List<T> obterPorFiltro(String filtro, String ordenacao) throws DataBaseException {
        return entityManager.getByWhere(tClass, filtro, ordenacao);
    }

    public BaseServico(Context context, Class<T> tClass) {
        this.entityManager = new EntityManager(context);
        this.tClass = tClass;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }
}
