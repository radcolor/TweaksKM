package com.theradcolor.kernel.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.theradcolor.kernel.R;
import com.theradcolor.kernel.activities.FAQsActivity;
import com.theradcolor.kernel.activities.OSLActivity;

public class AboutFragment extends Fragment implements View.OnClickListener{

    View root;
    private LinearLayout faqs, osl;
    private ImageView imageView, ghimg;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_about, container, false);

        faqs = root.findViewById(R.id.ll_faqs);
        osl = root.findViewById(R.id.ll_osl);
        faqs.setOnClickListener(this);
        osl.setOnClickListener(this);
        imageView =root. findViewById(R.id.tg_link);
        imageView.setOnClickListener(this);
        ghimg = root.findViewById(R.id.gh_link);
        ghimg.setOnClickListener(this);

        return root;
    }

    public void openTG() {
        Uri uri = Uri.parse("http://t.me/tweaksKM");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        //intent.setPackage("org.thunderdog.challegram");
        startActivity(intent);
    }

    public void openGH() {
        Uri uri = Uri.parse("http://github.com/theradcolor/tweaks");
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
            case R.id.ll_faqs:
                startActivity(new Intent(getContext(), FAQsActivity.class));
                break;
            case R.id.ll_osl:
                startActivity(new Intent(getContext(), OSLActivity.class));
                break;
        }
    }
}
