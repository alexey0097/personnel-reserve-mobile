package ru.iworking.personnel.reserve.mobile.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import ru.iworking.personnel.reserve.mobile.R;
import ru.iworking.personnel.reserve.mobile.dao.ResumeDao;
import ru.iworking.personnel.reserve.mobile.dao.ResumeDaoImpl;
import ru.iworking.personnel.reserve.mobile.entity.Resume;
import ru.iworking.personnel.reserve.mobile.exception.ObjectNotFoundException;
import ru.iworking.personnel.reserve.mobile.util.ImageUtils;

import static ru.iworking.personnel.reserve.mobile.util.IntentProperties.RESUME_EDIT;
import static ru.iworking.personnel.reserve.mobile.util.IntentProperties.RESUME_VIEW;

public class ResumeViewActivity extends AppCompatActivity {

    private final ResumeDao resumeDao = ResumeDaoImpl.getInstance(this);

    private Optional<Resume> resumeOptional = Optional.absent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_view);
        this.setupActionBar();

        String resumeStr = getIntent().getStringExtra(RESUME_VIEW);
        resumeOptional = Optional.fromNullable(new Gson().fromJson(resumeStr, Resume.class));
        Log.i(getClass().getSimpleName(), resumeOptional.toString());

        if (resumeOptional.isPresent()) {
            this.initData(resumeOptional.get());

            Button buttonResumeDelete = findViewById(R.id.buttonResumeDelete);
            buttonResumeDelete.setOnClickListener(view -> {
                resumeDao.deleteById(resumeOptional.get().getId());
                this.finish();
            });

            Button buttonResumeEdit = findViewById(R.id.buttonResumeEdit);
            buttonResumeEdit.setOnClickListener(view -> {
                Intent intent = new Intent(view.getContext(), ResumeEditActivity.class);
                intent.putExtra(RESUME_EDIT, new Gson().toJson(resumeOptional.get()));
                this.startActivityForResult(intent, 300);
            });
        } else {
            throw new ObjectNotFoundException("RESUME VIEW NOT FOUND!");
        }


    }

    private void updateData() {
        if (resumeOptional.isPresent()) {
            resumeOptional = resumeDao.findById(resumeOptional.get().getId());
            if (resumeOptional.isPresent()) this.initData(resumeOptional.get());
        }
    }

    private void initData(Resume resume) {
        TextView textViewFirstName = findViewById(R.id.textViewFirstName);
        TextView textViewLastName = findViewById(R.id.textViewLastName);
        TextView textViewMiddleName = findViewById(R.id.textViewMiddleName);
        TextView textViewProfession = findViewById(R.id.textViewProfession);
        TextView textViewAboutMe = findViewById(R.id.textViewAboutMe);
        ImageView avatarViewImage = findViewById(R.id.avatarViewImage);

        textViewFirstName.setText(resume.getFirstName());
        textViewLastName.setText(resume.getLastName());
        textViewMiddleName.setText(resume.getMiddleName());
        textViewProfession.setText(resume.getProfession());
        textViewAboutMe.setText(resume.getAboutMe());
        if (resume.getAvatar() != null && resume.getAvatar().length > 0) {
            avatarViewImage.setImageBitmap(ImageUtils.toBitmap(resume.getAvatar()));
        }
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 300) {
            this.updateData();
        }
    }

}
