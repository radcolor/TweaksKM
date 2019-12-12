package com.theradcolor.kernel.ui.About;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.theradcolor.kernel.FAQsActivity;
import com.theradcolor.kernel.R;

public class AboutFragment extends Fragment implements View.OnClickListener{

    private AboutViewModel notificationsViewModel;
    private ImageView imageView,ghimg;
    private TextView textView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(AboutViewModel.class);
        View root = inflater.inflate(R.layout.fragment_about, container, false);

        imageView =root. findViewById(R.id.tg_link);
        imageView.setOnClickListener(this);
        ghimg = root.findViewById(R.id.gh_link);
        ghimg.setOnClickListener(this);
        textView = root.findViewById(R.id.faqs);
        textView.setOnClickListener(this);

        //final TextView textView = root.findViewById(R.id.text_notifications);
        notificationsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });
        return root;
    }

    public void openTG() {
        Uri uri = Uri.parse("http://t.me/radkernelgroup");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        //intent.setPackage("org.thunderdog.challegram");
        startActivity(intent);
    }

    public void openGH() {
        Uri uri = Uri.parse("http://github.com/theradcolor");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        int view = v.getId();
        switch (view) {
            case R.id.tg_link:
                openTG();
                break;
            case R.id.gh_link:
                openGH();
                break;
            case R.id.faqs:
                startActivity(new Intent(getContext(), FAQsActivity.class));
                break;
        }
    }
}
