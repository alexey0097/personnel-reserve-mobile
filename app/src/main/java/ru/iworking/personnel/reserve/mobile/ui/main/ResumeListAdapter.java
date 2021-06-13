package ru.iworking.personnel.reserve.mobile.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import ru.iworking.personnel.reserve.mobile.R;
import ru.iworking.personnel.reserve.mobile.entity.Resume;

import java.util.List;

public class ResumeListAdapter extends BaseAdapter {

    private final Context context;
    private final List<Resume> values;
    private LayoutInflater layoutInflater;

    public ResumeListAdapter(Context context, List<Resume> values) {
        this.context = context;
        this.values = values;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public Object getItem(int position) {
        return values.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.list_view_row_resume, null);

        Resume resume = values.get(position);

        TextView textViewRowProfession = view.findViewById(R.id.textViewRowProfession);
        textViewRowProfession.setText(resume.getProfession());

        TextView textViewRowFio = view.findViewById(R.id.textViewRowFio);
        textViewRowFio.setText(resume.getFullName());

        return view;
    }

}
