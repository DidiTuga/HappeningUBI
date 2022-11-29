package pt.ubi.di.pdm.happening;

import android.app.ActionBar;
import android.content.Context;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class Uteis {

    public static void MSG(Context Cont, String txt)
    {
        Toast.makeText(Cont, txt, Toast.LENGTH_LONG).show();
    }
    public static void MSG_Debug(String txt)
    {
        Log.i("DEBUG", txt);
    }

    public static void MSG_Log(String txt){
        Log.i("INFO",txt);
        //  Categoria -- Depois posso meteter Passo 3 ect
    }
}
    /* METER APLICAÇÂO EM FULLSCREEN
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //TIRAR A BARRA DE TITULO
        Objects.requireNonNull(getSupportActionBar()).hide();*/
