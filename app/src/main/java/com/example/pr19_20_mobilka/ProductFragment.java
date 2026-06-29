package com.example.pr19_20_mobilka;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

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


public class ProductFragment extends Fragment {

    EtxCustom etName, etDescription, etExpenditure, etPrice;
    Spinner sCategory;
    BtnBig btnCreate;
    View btnImageSelect;
    BottomSheetHelper bottomSheetHelper;
    String currentPhotoPath;
    Uri imageUri;
    public Context context;
    public ProductFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);

        etName = view.findViewById(R.id.etName);
        etDescription = view.findViewById(R.id.etDescription);
        etExpenditure = view.findViewById(R.id.etExpenditure);
        etPrice = view.findViewById(R.id.etPrice);
        sCategory = view.findViewById(R.id.sCategory);
        btnCreate = view.findViewById(R.id.btnCreate);
        btnImageSelect = view.findViewById(R.id.btnImageSelect);

        bottomSheetHelper = new BottomSheetHelper(context);

        etName.init("название", EtxCustom.TypeEtx.DEFAULT);
        etDescription.init("описание", EtxCustom.TypeEtx.DEFAULT);
        etExpenditure.init("расход", EtxCustom.TypeEtx.DEFAULT);
        etPrice.init("стоимость", EtxCustom.TypeEtx.DEFAULT);
        btnCreate.init("подтвердить", BtnCustom.TypeButton.PRIMARY);

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
                    context, // Контекст
                    MainActivity.TOKEN, // Токен авторизации
                    product, // объект продукта
                    imageUri, // URI выбранного изображения
                    new MyResponseCallback() { // Callback для обработки результата
                        @Override
                        public void onCompile(String result) {
                            Log.e("PRODUCT CREATE:", result); // Логируем ответ сервера
                            Toast.makeText(context, "Новый продукт создан!", Toast.LENGTH_SHORT).show();
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
        return view;
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
                    context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) // Директория для сохранения
            );
            // Сохраняем абсолютный путь к файлу
            currentPhotoPath = PhotoFile.getAbsolutePath();
            // Получаем URI для файла с использованием FileProvider
            // Это необходимо для безопасного доступа к файлу (начиная с Android 7.0)
            imageUri = FileProvider.getUriForFile(
                    context,
                    context.getPackageName() + ".provider", // Authority из файла манифеста
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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

            if (!isCorrectPrice) {
                state = false;
                Toast.makeText(ProductActivity.init, "Поле принимает только цифры!", Toast.LENGTH_SHORT).show();
                etPrice.etx.setText("");
            }

            // Устанавливаем активность кнопки в зависимости от валидности всех полей
            btnCreate.setEnabled(state);
        }
    };
}