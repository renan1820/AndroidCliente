package com.example.renan.cliente.Util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Hermanos 04 on 09/11/2017.
 */

public class TratamentoImagem {

    public Bitmap carregarBitmap(int alturaDesejada, int larguraDesejada, int imagem, Resources resources) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;


        BitmapFactory.decodeResource(resources, imagem, options);

        final int altura = options.outHeight;
        final int largura = options.outWidth;

        if (altura > alturaDesejada || largura > larguraDesejada) {

            final  int taxaAltura = Math.round((float) altura / (float) alturaDesejada);
            final  int taxaLargura = Math.round((float) largura / (float) larguraDesejada);

            options.inSampleSize = taxaAltura < taxaLargura ? taxaAltura : taxaAltura;
        }

        options.inJustDecodeBounds = false;


        return  BitmapFactory.decodeResource(resources, imagem, options);

    }

}