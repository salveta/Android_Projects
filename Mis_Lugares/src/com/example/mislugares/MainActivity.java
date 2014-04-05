package com.example.mislugares;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;

//Todo listView debe heredar de ListActivity
public class MainActivity extends ListActivity {
	
	public BaseAdapter adaptador;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);//.edicion_lugar);//
		/**rea las vistas a través del Array ArrayAdapter<String> (this, 
		   R.layout.elemento_lista,
	       R.id.nombre,
         Lugares.listaNombres()); ahora substituido por AdaptadorLugares**/
		adaptador = new MiAdaptador (this); 
		 //Llama a setListAdaptar para indicar la lista de elementos a visualizar
		 setListAdapter(adaptador);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

          
     @Override public boolean onOptionsItemSelected(MenuItem item) {
 		switch (item.getItemId()) {
 		case R.id.acercaDe:
 			lanzarAcercaDe(null);
 			break;
 		case R.id.config:
 			lanzarPreferencias(null);
 			break;
 		}
 		return true; /** true -> consumimos el item, no se propaga*/
     }

     public void lanzarAcercaDe(View view){
     	Intent i = new Intent(this, AcercaDe.class);
 		startActivity(i);
     }

     public void lanzarPreferencias(View view){
      	Intent i = new Intent(this, Preferencias.class);
  		startActivity(i);
      }

     public void lanzarVistaLugar(View view){
    	     	 
    	 final EditText entrada = new EditText(this);
    	 entrada.setText("0");
    	 new AlertDialog.Builder(this)
    	    .setTitle("Selección de lugar")
    	    .setMessage("indica su id:")
    	    .setView(entrada)
    	    .setView(entrada)
    	    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
    	        public void onClick(DialogInterface dialog, int whichButton) {
    	            long id = Long.parseLong( entrada.getText().toString());
    	        	Intent i = new Intent(MainActivity.this, VistaLugar.class);
    	      		i.putExtra("id", id);
    	        	startActivity(i);    	            
    	        }})
    	    .setNegativeButton("Cancelar", null)
    	    .show();    	 
      }

     //Al usar el metodo onListItemClick este método deja de usarse
//     public void _lanzarVistaLugar(View view){
//    	Intent i = new Intent(this, VistaLugar.class);
//  		i.putExtra("id", (long)0);
//    	startActivity(i);
//      }

     @Override 
     protected void onListItemClick(ListView listView, View vista, int posicion, long id) {
        super.onListItemClick(listView, vista, posicion, id);
        Intent intent= new Intent(this, VistaLugar.class);
        intent.putExtra("id", id);
        startActivity(intent);
//        Para lanzar varias clases al pulsar
//        if (posicion == 0 ){
//            startActivity(new Intent(getApplicationContext(), CLASE.class));
//            }else
//         if (posicion == 1){
//            startActivity(new Intent(getApplicationContext(), CLASE.class));
//            }
     }
}
