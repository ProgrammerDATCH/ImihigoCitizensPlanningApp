package com.imihigocizitensplanning.app.UsefullClasses;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class NationalIDFormatter {
    private static boolean isFormatting = false;

    public static void formatNationalID(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (isFormatting) {
                    return;
                }

                isFormatting = true;

                String inputValue = editable.toString().replaceAll("[^\\d]", "");
                StringBuilder formattedValue = new StringBuilder();
                int j = 0;

                // Define your format pattern
                String formatPattern = "- ---- - ------- - --";

                for (int i = 0; i < formatPattern.length(); i++) {
                    if (formatPattern.charAt(i) == ' ') {
                        formattedValue.append(" ");
                    } else {
                        if (j < inputValue.length()) {
                            formattedValue.append(inputValue.charAt(j));
                            j++;
                        } else {
                            if (i == 0 && formattedValue.length() == 0) {
                                formattedValue.append("-");
                            } else {
                                formattedValue.append("-");
                            }
                        }
                    }
                }

                editText.removeTextChangedListener(this);
                editText.setText(formattedValue.toString());

                // Set the cursor position within bounds
                int cursorPosition = formattedValue.length();
                if (cursorPosition > editText.length()) {
                    cursorPosition = editText.length();
                }

                editText.setSelection(cursorPosition);
                editText.addTextChangedListener(this);

                isFormatting = false;
            }
        });
    }
}
