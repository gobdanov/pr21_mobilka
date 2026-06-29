package com.example.pr19_20_mobilka;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.example.network.datas.products.ProductCreate;
import com.example.network.domains.callbacks.MyResponseCallback;
import com.example.network.domains.models.Product;
import com.example.uicomponents.button.BtnBig;
import com.example.uicomponents.button.BtnCustom;
import com.example.uicomponents.button.EtxCustom;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

import presentations.BottomSheetHelper;

public class ProductActivity extends AppCompatActivity {
    public static ProductActivity init;
    public Fragment openFragment;
    EtxCustom etName, etDescription, etExpenditure, etPrice;
    Spinner sCategory;
    BtnBig btnCreate;
    View btnImageSelect;
    BottomSheetHelper bottomSheetHelper;
    String currentPhotoPath;
    Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        init = this;

        etName = findViewById(R.id.etName);
        etDescription = findViewById(R.id.etDescription);
        etExpenditure = findViewById(R.id.etExpenditure);
        etPrice = findViewById(R.id.etPrice);
        sCategory = findViewById(R.id.sCategory);
        btnCreate = findViewById(R.id.btnCreate);
        btnImageSelect = findViewById(R.id.btnImageSelect);



        bottomSheetHelper = new BottomSheetHelper(this);

        etName.init("название", EtxCustom.TypeEtx.DEFAULT);
        etDescription.init("описание", EtxCustom.TypeEtx.DEFAULT);
        etExpenditure.init("расход", EtxCustom.TypeEtx.DEFAULT);
        etPrice.init("стоимость", EtxCustom.TypeEtx.DEFAULT);

        btnCreate.init("подтвердить", BtnCustom.TypeButton.PRIMARY);

        View.OnFocusChangeListener LastFocus = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // Если поле получило фокус, ничего не делаем (ждем окончания ввода)
                if (hasFocus) return;
                // Флаг валидности всех полей
                boolean state = true;
                // Проверяем, что все текстовые поля заполнены
                if (etName.etx.getText().toString().isEmpty()) state = false;
                if (etDescription.etx.getText().toString().isEmpty()) state = false;
                if (etExpenditure.etx.getText().toString().isEmpty()) state = false;
                if (etPrice.etx.getText().toString().isEmpty()) state = false;
                // Проверяем, что поле цены содержит только цифры
                boolean isCorrectPrice = Pattern.matches("\\d+", etPrice.etx.getText().toString());

                if (!isCorrectPrice){
                    state = false;
                    Toast.makeText(ProductActivity.init,"Поле принимает только цифры!", Toast.LENGTH_SHORT).show();
                    etPrice.etx.setText("");
                }

                // Устанавливаем активность кнопки в зависимости от валидности всех полей
                btnCreate.setEnabled(state);
            }


        };

        etPrice.etx.setOnFocusChangeListener(LastFocus);

        btnImageSelect.setOnClickListener(v->{
            bottomSheetHelper.dialog.show();
        });

        btnCreate.Btn.setOnClickListener(v -> {
            // Создаем объект Product из данных, введенных пользователем

            int sex = 1;
            if (sCategory.getSelectedItem().toString() != "Мужской"){
                sex =0;
            }

            Product product = new Product(
                    etName.etx.getText().toString(), // Название
                    etDescription.etx.getText().toString(), // Описание
                    sex, // Категория (позиция в списке)
                    etExpenditure.etx.getText().toString(), // Расход
                    Integer.parseInt(etPrice.etx.getText().toString()) // Цена (преобразуем в целое число)
            );

            // Создаем и выполняем асинхронную задачу для отправки продукта на сервер
            ProductCreate RequestProductCreate = new ProductCreate(
                     this, // Контекст
                    MainActivity.TOKEN, // Токен авторизации
                    product, // объект продукта
                    imageUri, // URI выбранного изображения
                    new MyResponseCallback() { // Callback для обработки результата
                        @Override
                        public void onCompile(String result) {
                            Log.e("PRODUCT CREATE:", result); // Логируем ответ сервера
                            Toast.makeText(init, "Новый продукт создан!", Toast.LENGTH_SHORT).show();
                            finish(); // Закрываем активность и возвращаемся на главный экран
                        }
                        @Override
                        public void onError(String error) {
                            Log.e("PRODUCT CREATE ERROR:", error);
                        }
                    }
                    );

            // Запускаем асинхронную задачу
            RequestProductCreate.execute();
        });
    }

    public void OpenGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");    // Фильтр только для изображений
        intent.setAction(Intent.ACTION_GET_CONTENT);    // Действие - получение контента
        startActivityForResult(Intent.createChooser(intent, "Выберите изображение"), 1);
    }

    public void OpenCamera() {
        try {
            // Создаем Intent для запуска камеры
            Intent PictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Создаем временный файл для сохранения фотографии
            File PhotoFile = File.createTempFile(
                    "MY_PHOTO_CADR", // Префикс имени файла
                    ".jpg", // Расширение файла
                    getExternalFilesDir(Environment.DIRECTORY_PICTURES) // Директория для сохранения
        );
            // Сохраняем абсолютный путь к файлу
            currentPhotoPath = PhotoFile.getAbsolutePath();
            // Получаем URI для файла с использованием FileProvider
            // Это необходимо для безопасного доступа к файлу (начиная с Android 7.0)
            imageUri = FileProvider.getUriForFile(
                    this,
                    getPackageName() + ".provider", // Authority из файла манифеста
                    PhotoFile
        );
            // Добавляем файл для временного доступа к URI
            PictureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            PictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            // Указываем, куда сохранить сделанное фото
            PictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            // Запускаем активность камеры с ожиданием результата
            startActivityForResult(PictureIntent,2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Закрываем BottomSheet диалог после выбора изображения
        bottomSheetHelper.dialog.cancel();
        // Если операция выполнена успешно
        if (resultCode == RESULT_OK) {
            // Если изображение выбрано из галереи (requestCode = 1)
            if (requestCode == 1)
                imageUri = data.getData(); // Получаем URI выбранного изображения
                // Отображаем выбранное изображение в кнопке (ImageView)
                ((ImageView) btnImageSelect).setImageURI(imageUri);

        }
    }
}
