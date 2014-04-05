package com.example.mislugares;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class MiAdaptador extends BaseAdapter {

	private LayoutInflater inflador; // Crea Layouts a partir del XML
    TextView nombre, direccion;
    ImageView foto;
    RatingBar valoracion;
      public MiAdaptador (Context contexto) {
            inflador =(LayoutInflater)contexto
                   .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public View getView(int posicion, View vistaReciclada, 
                                       ViewGroup padre) {
         Lugar lugar =Lugares.elemento(posicion);
         if (vistaReciclada == null) {
              vistaReciclada= inflador.inflate(R.layout.elemento_lista, null);
         }
                nombre = (TextView) vistaReciclada.findViewById(R.id.nombre);
         direccion = (TextView) vistaReciclada.findViewById(R.id.direccion);
         foto = (ImageView) vistaReciclada.findViewById(R.id.foto);
         valoracion = (RatingBar) vistaReciclada.findViewById(R.id.valoracion);
         nombre.setText(lugar.getNombre());
         direccion.setText(lugar.getDireccion());
         int id = R.drawable.otros;
         switch(lugar.getTipo()) {
           case RESTAURANTE:id = R.drawable.restaurante; break;
           case BAR:        id = R.drawable.bar;         break;
           case COPAS:      id = R.drawable.copas;       break;
           case ESPECTACULO:id = R.drawable.espectaculos; break;
           case HOTEL:      id = R.drawable.hotel;       break;
           case COMPRAS:    id = R.drawable.compras;     break;
           case EDUCACION:  id = R.drawable.educacion;   break;
           case DEPORTE:    id = R.drawable.deporte;     break;
           case NATURALEZA: id = R.drawable.naturaleza;  break;
           case GASOLINERA: id = R.drawable.gasolinera;  break;
         }

         foto.setImageResource(id);
         foto.setScaleType(ImageView.ScaleType.FIT_END);
         valoracion.setRating(lugar.getValoracion());  
         return vistaReciclada;
      }

      public int getCount() {
         return Lugares.size();
      }
public Object getItem(int posicion) {
   return Lugares.elemento(posicion);
}

public long getItemId(int posicion) {
   return posicion;
}

/**
 * En el constructor de la clase creamos un inflater en el objeto inflador. 
 * Un inflater es una herramienta que nos permite crear un objeto Java a partir de un fichero XML que lo describe. 
 * El el ejemplo cremaos un inflater para layouts.
 * En esta clase el m�todo m�s importante es getView(), que usa el sistema para pedir cada uno de los elementos a insertar. 
 * Cuando se llame a getView(), nos indicar�n tres par�metros: la posici�n del elemento a insertar, una vista reciclada y el layout 
 * contenedor donde se insertar� el elemento. Este m�todo ha de devolver una vista con la informaci�n adecuada del elemento 
 * a insertar. El par�metro vistaReciclada se utiliza para mejorar el rendimiento en la creaci�n de vistas. 
 * Para la primera llamada a getView(), este par�metro ser� nulo y tendremos que crear una nueva vista e inflarla 
 * con el inflater desde un XML (este proceso puede ser algo lento). Pero para las siguientes llamadas, este par�metro contendr�
 *  la vista devuelta por nosotros en la llamada anterior, para esta posici�n. 
 *  De esta forma ya no ser� necesario crearla desde cero y solo tendremos que modificar sus caracter�sticas y devolverla. 
 *  El reto del m�todo se utiliza para actualizar cada campo, seg�n el lugar a representar.
 */

}