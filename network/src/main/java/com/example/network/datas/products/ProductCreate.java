package com.example.network.datas.products;

import android.content.Context;
import android.net.Uri;

import com.example.network.domains.apis.MyAsyncTask;
import com.example.network.domains.callbacks.MyResponseCallback;
import com.example.network.domains.common.Settings;
import com.example.network.domains.models.Product;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ProductCreate extends MyAsyncTask {
    private Context context;
    private String token;
    private Product product;
    private Uri uri;

    public ProductCreate(Context context, String token, Product product, Uri uri, MyResponseCallback callback) {
        super(callback);

        this.context = context;
        this.token = token;
        this.product = product;
        this.uri = uri;
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            if (context == null) {
                return "Error: Context is null!";
            }

            if (uri == null) {
                return "Error: Image URI is null!";
            }

            // Теперь context точно не null
            InputStream inputStream = context.getContentResolver().openInputStream(uri);

            if (inputStream == null) {
                return "Error: Could not open input stream!";
            }

            File tempFile = createTempFileFromServer(inputStream);

            if (tempFile == null) {
                return "Error: Could not create temp file!";
            }

            Map<String, String> params = new HashMap<>();
            params.put("Name", product.name);
            params.put("Description", product.description);
            params.put("Gender", String.valueOf(product.gender));
            params.put("Expenditure", product.expenditure);
            params.put("Price", String.valueOf(product.price));

            Connection.Response response = Jsoup.connect(Settings.Url + "product/create")
                    .ignoreContentType(true)
                    .method(Connection.Method.POST)
                    .header("token", token)
                    .data(params)
                    .data("ImageFile", tempFile.getName(), new java.io.FileInputStream(tempFile))
                    .execute();

            return response.statusCode() == 200 ? response.body() : "1.PRODUCT CREATE Error: " + response.body();

        } catch (IOException e) {
            return "2.PRODUCT CREATE Error: " + e.getMessage();
        }
    }

    public File createTempFileFromServer(InputStream inputStream) {
        try {
            File tempFile = File.createTempFile("upload_", ".jpg");
            tempFile.deleteOnExit();

            try (FileOutputStream out = new FileOutputStream(tempFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }

            return tempFile;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}