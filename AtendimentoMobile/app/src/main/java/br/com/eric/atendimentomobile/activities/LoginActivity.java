package br.com.eric.atendimentomobile.activities;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import br.com.eric.atendimentomobile.MainActivity;
import br.com.eric.atendimentomobile.R;
import br.com.eric.atendimentomobile.entidade.AtendimentoMobile;
import br.com.eric.atendimentomobile.entidade.ED2DCodigoResponse;
import br.com.eric.atendimentomobile.entidade.envio.MobileEnvioLogin;
import br.com.eric.atendimentomobile.entidade.retorno.MobileRetorno;
import br.com.eric.atendimentomobile.entidade.retorno.MobileRetornoLogin;
import br.com.eric.atendimentomobile.servico.MobileEnvioServico;
import br.com.eric.atendimentomobile.utils.EntityManager;
import br.com.eric.atendimentomobile.utils.UtilActivity;

public class LoginActivity extends Activity {

    private EditText loginET;
    private EditText senhaET;
    private Spinner spinnerAplicacao;
    private Button loginBT;
    private FrameLayout logoFL;
    private LinearLayout camposLogin;
    private LinearLayout camposAplicacao;
    private RelativeLayout preLoaderLoginRL;
    private Boolean showAnimations = true;
    private MobileEnvioServico mobileEnvioServicoLogin;
    private EntityManager entityManager;

    private AtendimentoMobile atendimentoMobile;

    public AsyncTask<Void, Void, MobileRetorno> taskLogar;
    private Integer idAgendamento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        carregarCamposEntrada();
        mobileEnvioServicoLogin = new MobileEnvioServico(MobileRetornoLogin.class);
        entityManager = new EntityManager(this);
        atendimentoMobile = (AtendimentoMobile) getApplication();
        if (showAnimations) {
            camposLogin.setVisibility(View.GONE);
            camposLogin.animate().setDuration(0);
            camposLogin.animate().alpha(0).setListener(new Animator.AnimatorListener() {

                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (showAnimations) {
                        camposLogin.setVisibility(View.VISIBLE);
                        camposLogin.animate().setStartDelay(900);
                        camposLogin.animate().setDuration(450);
                        camposLogin.animate().alpha(1);
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            logoFL = (FrameLayout) findViewById(R.id.logoFL);
            logoFL.setVisibility(View.GONE);
            logoFL.animate().setDuration(0);
            logoFL.animate().translationY(250).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (showAnimations) {
                        logoFL.animate().setDuration(0);
                        logoFL.animate().alpha(0).setListener(new Animator.AnimatorListener() {

                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                if (showAnimations) {
                                    logoFL.setVisibility(View.VISIBLE);
                                    logoFL.animate().setDuration(750);
                                    logoFL.animate().alpha(1).setListener(new Animator.AnimatorListener() {

                                        @Override
                                        public void onAnimationStart(Animator animation) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            if (showAnimations) {
                                                logoFL.animate().setDuration(450);
                                                logoFL.animate().translationY(0);
                                                showAnimations = false;
                                            }
                                        }

                                        @Override
                                        public void onAnimationCancel(Animator animation) {

                                        }

                                        @Override
                                        public void onAnimationRepeat(Animator animation) {

                                        }
                                    });
                                }
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
    }

    public void carregarCamposEntrada() {
        loginET = (EditText) findViewById(R.id.loginET);
        senhaET = (EditText) findViewById(R.id.senhaET);
        spinnerAplicacao = (Spinner) findViewById(R.id.spinnerAplicacao);
        loginBT = (Button) findViewById(R.id.loginBT);
        preLoaderLoginRL = (RelativeLayout) findViewById(R.id.preLoaderLoginRL);
        camposLogin = (LinearLayout) findViewById(R.id.camposLoginLL);
        camposAplicacao = (LinearLayout) findViewById(R.id.camposAplicacaoLL);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
        Calendar dataAtual = Calendar.getInstance();
        String anoAtual = simpleDateFormat.format(dataAtual.getTime());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return false;
    }

    public void logar(View view) {
        camposLogin.setVisibility(View.GONE);
        camposAplicacao.setVisibility(View.GONE);
        preLoaderLoginRL.setVisibility(View.VISIBLE);

        final MobileEnvioLogin mobileEnvioLogin = new MobileEnvioLogin(
                atendimentoMobile, loginET.getText().toString(),
                senhaET.getText().toString());

        try {
            taskLogar = new AsyncTask<Void, Void, MobileRetorno>() {
                @Override
                protected MobileRetorno doInBackground(Void... params) {
                    MobileRetorno mobileRetorno = null;
                    try {
                        mobileRetorno = mobileEnvioServicoLogin.send(mobileEnvioLogin);
                    } catch (Exception e) {
                        e.printStackTrace();
                        mobileRetorno = new MobileRetornoLogin();
                        return mobileRetorno;
                    }
                    return mobileRetorno;
                }

                @Override
                protected void onPostExecute(MobileRetorno resposta) {
                    super.onPostExecute(resposta);
                    if (resposta.getCodigoRetorno() != null && resposta.getCodigoRetorno().equals(ED2DCodigoResponse.OK.toString())) {
                        try {
                            MobileRetornoLogin mobileRetornoLogin = (MobileRetornoLogin) resposta;

                            Intent destino = new Intent(getApplicationContext(), MainActivity.class);

                            startActivity(destino);
                            LoginActivity.this.finish();
                        } catch (Exception e) {
                            camposLogin.setVisibility(View.VISIBLE);
                            preLoaderLoginRL.setVisibility(View.GONE);
                            UtilActivity.makeShortToast("O login e/a senha que você digitou não coincidem.", getApplicationContext());
                        }
                    } else {
                        UtilActivity.makeShortToast(resposta.getMensagem(), getApplicationContext());
                        camposLogin.setVisibility(View.VISIBLE);
                        preLoaderLoginRL.setVisibility(View.GONE);
                    }
                }
            };
            taskLogar.execute();
        } catch (Exception e) {
            UtilActivity.makeShortToast("Não foi possível autenticar.", getApplicationContext());
            camposLogin.setVisibility(View.VISIBLE);
            preLoaderLoginRL.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        camposLogin.setVisibility(View.VISIBLE);
        preLoaderLoginRL.setVisibility(View.GONE);
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
        System.exit(0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
