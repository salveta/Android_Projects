package com.example.mislugares;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

public class VistaLugar extends Activity {
	private long id;
	private Lugar lugar;
	private ImageView imageView;
	private Uri uriFoto;
	final static int RESULTADO_EDITAR= 1;
	final static int RESULTADO_GALERIA= 2;
	final static int RESULTADO_FOTO= 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vista_lugar);
		Bundle extras = getIntent().getExtras();
		id = extras.getLong("id", -1);
		lugar = Lugares.elemento((int) id);
		imageView = (ImageView) findViewById(R.id.foto);
		actualizarVistas();
	}

	private void actualizarVistas() {
		TextView nombre = (TextView) findViewById(R.id.nombre);
		nombre.setText(lugar.getNombre());
		ImageView logo_tipo = (ImageView) findViewById(R.id.logo_tipo);
		logo_tipo.setImageResource(lugar.getTipo().getRecurso());
		TextView tipo = (TextView) findViewById(R.id.tipo);
		tipo.setText(lugar.getTipo().getTexto());
		TextView direccion = (TextView) findViewById(R.id.direccion);
		direccion.setText(lugar.getDireccion());
		if (lugar.getTelefono() == 0) {
			findViewById(R.id.p_telefono).setVisibility(View.GONE);
		} else {
			TextView telefono = (TextView) findViewById(R.id.telefono);
			telefono.setText(Integer.toString(lugar.getTelefono()));
		}
		TextView url = (TextView) findViewById(R.id.url);
		url.setText(lugar.getUrl());
		TextView comentario = (TextView) findViewById(R.id.comentario);
		comentario.setText(lugar.getComentario());
		TextView fecha = (TextView) findViewById(R.id.fecha);
		fecha.setText(DateFormat.getDateInstance().format(
				new Date(lugar.getFecha())));
		TextView hora = (TextView) findViewById(R.id.hora);
		hora.setText(DateFormat.getTimeInstance().format(
				new Date(lugar.getFecha())));
		RatingBar valoracion = (RatingBar) findViewById(R.id.valoracion);
		valoracion.setRating(lugar.getValoracion());
		valoracion
				.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
					@Override
					public void onRatingChanged(RatingBar ratingBar,
							float valor, boolean fromUser) {
						lugar.setValoracion(valor);
					}
				});
		//Visualizamos la foto cambiada al crear la actividad
		ponerFoto(imageView, lugar.getFoto());

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.vista_lugar, menu);
		return true;
	}

	/*
	Si el item de menú seleccionado coresponde al id accion_compartir se creará una intención implicita con acción ACTION_SEND. 
	Mandaremos un texto plano formado por el nombre del lugar y su URL. 
	Esta información podrá ser recogida por cualquier aplicación que se haya registrado como enviadora de mensages 
	(WhatsApp, Gmail, SMS, …). El siguiente case asociado al id accion_llegar hará que se llame al método verMapa().
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.accion_compartir:
			 Intent intent = new Intent(Intent.ACTION_SEND);
             intent.setType("text/plain");
             intent.putExtra(Intent.EXTRA_TEXT,
                lugar.getNombre() + " - "+ lugar.getUrl());
             startActivity(intent);
			return true;
		case R.id.accion_llegar:
			verMapa(null);
			return true;
		case R.id.accion_editar:
			Intent intent2 = new Intent(this, EdicionLugar.class);
			intent2.putExtra("id", id);
			startActivityForResult(intent2, RESULTADO_EDITAR);
			return true;
		case R.id.accion_borrar:
			borradoLugar();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	//Método, que nos permitirá ver un mapa con la posición del lugar:
	public void verMapa(View view) {
        Uri uri;
        double lat = lugar.getPosicion().getLatitud();
        double lon = lugar.getPosicion().getLongitud();
        if (lat != 0 || lon != 0) {
               uri = Uri.parse("geo:" + lat + "," + lon);
        } else {
               uri = Uri.parse("geo:0,0?q=" + lugar.getDireccion());
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
  }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == RESULTADO_EDITAR) {
			actualizarVistas();
			findViewById(R.id.scrollView1).invalidate();
		} else if (requestCode == RESULTADO_GALERIA
                && resultCode == Activity.RESULT_OK) {
				lugar.setFoto(data.getDataString());
				ponerFoto(imageView, lugar.getFoto());
		} else if(requestCode == RESULTADO_FOTO && resultCode == Activity.RESULT_OK
		        && lugar!=null && uriFoto!=null) {
		       lugar.setFoto(uriFoto.toString());
		       ponerFoto(imageView, lugar.getFoto());
		}
	}

	
	
	private void borradoLugar() {
		new AlertDialog.Builder(this)
				.setTitle("Borrado de lugar")
				.setMessage("¿Estás seguro que quieres eliminar este lugar?")
				.setCancelable(false)
				.setPositiveButton("Confirmar",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialogo, int _id) {
								Lugares.borrar((int) id);
								finish();
							}
						}).setNegativeButton("Cancelar", null).show();
	}

	//Agregamos metodos de intenciones
	public void llamadaTelefono(View view) {
	    startActivity(new Intent(Intent.ACTION_DIAL,
	                           Uri.parse("tel:" + lugar.getTelefono())));
	}
	 

	public void pgWeb(View view) {
	    startActivity(new Intent(Intent.ACTION_VIEW,
	                           Uri.parse(lugar.getUrl())));
	}
	
	/*
	 * Este método crea una intención indicando que queremos obtener contenido, que pueda ser abierto y que 
	 * además sea de tipo imagen y de cualquier subtipo. Tipicamente se abrirá la aplicación galería de fotos 
	 * (u otra similar). Como necesitamos una respuesta usamos startActivityForResult() con el código adecuado.
	 */
	
	//Código para poner foto junto con onActivityResul y sexta linea de oncreate
	
	protected void ponerFoto(ImageView imageView, String uri) {
	    if (uri != null) {
	        imageView.setImageURI(Uri.parse(uri));
	    } else {
	        imageView.setImageBitmap(null);
	        // poner foto por defecto
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.foto_epsg));
	    }
	}
	public void galeria(View view) {
	       Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
	       intent.addCategory(Intent.CATEGORY_OPENABLE);
	       intent.setType("image/*");
	       startActivityForResult(intent, RESULTADO_GALERIA);
	}
	//Metodo tomar fotos desde la camara
	public void tomarFoto(View view) {
	    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
	    uriFoto = Uri.fromFile(
	        new File(Environment.getExternalStorageDirectory() + File.separator
	        + "img_" + (System.currentTimeMillis() / 1000) + ".jpg"));
	    intent.putExtra(MediaStore.EXTRA_OUTPUT, uriFoto);
	    startActivityForResult(intent, RESULTADO_FOTO);
	}
	//Metodo para borrar fotos hechas con la cámara
	public void eliminarFoto(View view) {
	       lugar.setFoto(null);
	       ponerFoto(imageView, null);
	}
}
