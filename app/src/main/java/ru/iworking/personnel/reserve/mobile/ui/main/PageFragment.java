package ru.iworking.personnel.reserve.mobile.ui.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import ru.iworking.personnel.reserve.mobile.R;
import ru.iworking.personnel.reserve.mobile.activity.ResumeCreateActivity;
import ru.iworking.personnel.reserve.mobile.activity.ResumeViewActivity;
import ru.iworking.personnel.reserve.mobile.dao.ResumeDao;
import ru.iworking.personnel.reserve.mobile.dao.ResumeDaoImpl;
import ru.iworking.personnel.reserve.mobile.entity.Resume;

import java.math.BigDecimal;
import java.util.List;

import static ru.iworking.personnel.reserve.mobile.util.IntentProperties.RESUME_VIEW;

/**
 * A placeholder fragment containing a simple view.
 */
public class PageFragment extends Fragment {

    private static final String ARG_SECTION_PAGE = "section_page";

    private Button buttonRefresh;
    private int page;

    public static PageFragment newInstance(int index) {
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_PAGE, index);

        PageFragment fragment = new PageFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = 1;
        if (getArguments() != null) {
            page = getArguments().getInt(ARG_SECTION_PAGE);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        switch (page) {
            case 1: {
                return getResumesView(inflater, container, savedInstanceState);
            }
            case 2: {
                return getNdflView(inflater, container, savedInstanceState);
            }
            default: {
                return getResumesView(inflater, container, savedInstanceState);
            }
        }
    }

    public View getResumesView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_resumes, container, false);
        ListView resumeListView = root.findViewById(R.id.resumeListView);

        Button buttonAdd = root.findViewById(R.id.buttonCountNdfl);
        buttonAdd.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), ResumeCreateActivity.class);
            this.startActivityForResult(intent, 200);
        });

        buttonRefresh = root.findViewById(R.id.buttonRefresh);
        buttonRefresh.setOnClickListener(view -> {
            resumeListView.setAdapter(this.loadDataForResumeList(root));
        });

        resumeListView.setAdapter(this.loadDataForResumeList(root));
        resumeListView.setOnItemClickListener((adapter, view, position, id) -> {
            Object item = adapter.getItemAtPosition(position);
            Intent intent = new Intent(view.getContext(), ResumeViewActivity.class);
            intent.putExtra(RESUME_VIEW, new Gson().toJson(item));
            this.startActivityForResult(intent, 200);
        });

        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.worki_logo_100_100);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(bmp);
        bitmapDrawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);

        LinearLayout linearLayout = root.findViewById(R.id.linearLayout);
        linearLayout.setBackgroundDrawable(bitmapDrawable);

        return root;
    }

    private BaseAdapter loadDataForResumeList(View root) {
        ResumeDao resumeDao = ResumeDaoImpl.getInstance(root.getContext());
        List<Resume> list = resumeDao.findAll();
        Log.i(getClass().getSimpleName(), list.toString());

        return new ResumeListAdapter(root.getContext(), list);
    }

    public View getVacanciesView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_vacancies, container, false);
        return root;
    }

    public View getNdflView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_ndfl, container, false);

        Button buttonCountNdfl = root.findViewById(R.id.buttonCountNdfl);
        EditText editTextSalary = root.findViewById(R.id.editTextSalary);
        TextView ndflTextView = root.findViewById(R.id.ndflTextView);

        buttonCountNdfl.setOnClickListener(view -> {
            String salary = editTextSalary.getText().toString();
            if (!Strings.isNullOrEmpty(salary)) {
                Double countSalary = new Double(salary);
                Double ndfl = countSalary * 0.13;
                ndflTextView.setText(String.format("Размер НДФЛ = %.2f", ndfl));
            }
        });
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200) {
            if (buttonRefresh != null) buttonRefresh.performClick();
        }
    }

}