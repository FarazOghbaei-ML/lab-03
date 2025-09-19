package com.example.listycitylab3;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class AddCityFragment extends DialogFragment {

    interface AddCityDialogListener {
        void addCity(City city);
        void updateCity(int position, City city);
        void onDialogClosed();            // NEW: lets activity clear the guard flag
    }

    private AddCityDialogListener listener;

    private static final String ARG_NAME = "arg_name";
    private static final String ARG_PROVINCE = "arg_province";
    private static final String ARG_POSITION = "arg_position";

    public static AddCityFragment newInstance(@Nullable String name,
                                              @Nullable String province,
                                              int position) {
        AddCityFragment f = new AddCityFragment();
        Bundle b = new Bundle();
        if (name != null) b.putString(ARG_NAME, name);
        if (province != null) b.putString(ARG_PROVINCE, province);
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddCityDialogListener) {
            listener = (AddCityDialogListener) context;
        } else {
            throw new RuntimeException(context + " must implement AddCityDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(requireContext())
                .inflate(R.layout.fragment_add_city, null);

        EditText editCityName = view.findViewById(R.id.edit_text_city_text);
        EditText editProvinceName = view.findViewById(R.id.edit_text_province_text);

        int position = -1;
        Bundle args = getArguments();
        if (args != null) {
            position = args.getInt(ARG_POSITION, -1);
            if (position >= 0) {
                editCityName.setText(args.getString(ARG_NAME, ""));
                editProvinceName.setText(args.getString(ARG_PROVINCE, ""));
            }
        }
        final int pos = position;               // must be effectively final for lambda
        final boolean isEdit = pos >= 0;

        return new AlertDialog.Builder(requireContext())
                .setView(view)
                .setTitle(isEdit ? "Edit a city" : "Add a city")
                .setNegativeButton("Cancel", null)
                .setPositiveButton(isEdit ? "Save" : "Add", (dialog, which) -> {
                    String cityName = editCityName.getText().toString();
                    String provinceName = editProvinceName.getText().toString();
                    if (isEdit) {
                        listener.updateCity(pos, new City(cityName, provinceName));
                    } else {
                        listener.addCity(new City(cityName, provinceName));
                    }
                })
                .create();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if (listener != null) listener.onDialogClosed();   // clear guard in activity
    }
}