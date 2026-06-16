package presentations;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.example.pr19_20_mobilka.ProductActivity;
import com.example.pr19_20_mobilka.R;
import com.example.uicomponents.button.BtnBig;
import com.example.uicomponents.button.BtnCustom;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class BottomSheetHelper {
    public BottomSheetDialog dialog;

    public BottomSheetHelper(Context context) {
        dialog = new BottomSheetDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.bs_select_image, null);

        BtnBig btnGallery = view.findViewById(R.id.btnGallery);
        BtnBig btnCamera = view.findViewById(R.id.btnCamera);

        btnGallery.init("выбрать из галереи", BtnCustom.TypeButton.SECONDARY);
        btnCamera.init("сфотографировать", BtnCustom.TypeButton.SECONDARY);

        btnGallery.Btn.setOnClickListener(x -> {
            ProductActivity.init.OpenGallery();
        });

        btnCamera.Btn.setOnClickListener(x -> {
            ProductActivity.init.OpenCamera();
        });

        dialog.setContentView(view);
    }
}
