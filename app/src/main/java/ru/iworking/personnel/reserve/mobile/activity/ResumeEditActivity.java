package ru.iworking.personnel.reserve.mobile.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.google.common.base.Optional;
import com.google.gson.Gson;
import ru.iworking.personnel.reserve.mobile.R;
import ru.iworking.personnel.reserve.mobile.dao.ResumeDao;
import ru.iworking.personnel.reserve.mobile.dao.ResumeDaoImpl;
import ru.iworking.personnel.reserve.mobile.entity.Resume;
import ru.iworking.personnel.reserve.mobile.exception.ObjectNotFoundException;
import ru.iworking.personnel.reserve.mobile.util.ImageUtils;

import java.io.IOException;
import java.util.Objects;

import static ru.iworking.personnel.reserve.mobile.util.IntentProperties.RESUME_EDIT;
import static ru.iworking.personnel.reserve.mobile.util.IntentProperties.RESUME_VIEW;

public class ResumeEditActivity extends AppCompatActivity {

    private final ResumeDao resumeDao = ResumeDaoImpl.getInstance(this);

    private static final int GALLERY_REQUEST = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_edit);
        setupActionBar();

        String resumeStr = getIntent().getStringExtra(RESUME_EDIT);
        Optional<Resume> resumeOptional = Optional.fromNullable(new Gson().fromJson(resumeStr, Resume.class));
        Log.i(getClass().getSimpleName(), resumeOptional.toString());

        EditText editTextFirstName = findViewById(R.id.editTextFirstName);
        EditText editTextLastName = findViewById(R.id.editTextLastName);
        EditText editTextMiddleName = findViewById(R.id.editTextMiddleName);
        EditText editTextProfession = findViewById(R.id.editTextProfession);
        EditText editTextAboutMe = findViewById(R.id.editTextAbountMe);
        ImageView imageView = findViewById(R.id.avatarEditImageView);

        imageView.setOnClickListener(view -> {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
        });

        if (resumeOptional.isPresent()) {
            Resume oldResume = resumeOptional.get();
            editTextFirstName.setText(oldResume.getFirstName());
            editTextLastName.setText(oldResume.getLastName());
            editTextMiddleName.setText(oldResume.getMiddleName());
            editTextProfession.setText(oldResume.getProfession());
            editTextAboutMe.setText(oldResume.getAboutMe());
            if (oldResume.getAvatar() != null && oldResume.getAvatar().length > 0) {
                imageView.setImageBitmap(ImageUtils.toBitmap(oldResume.getAvatar()));
            }

            Button buttonAdd = findViewById(R.id.buttonEditResume);
            buttonAdd.setOnClickListener(view -> {
                Resume resume = new Resume();
                resume.setId(oldResume.getId());
                resume.setFirstName(editTextFirstName.getText().toString());
                resume.setLastName(editTextLastName.getText().toString());
                resume.setMiddleName(editTextMiddleName.getText().toString());
                resume.setProfession(editTextProfession.getText().toString());
                resume.setAboutMe(editTextAboutMe.getText().toString());

                Drawable drawable = imageView.getDrawable();
                if (drawable == null) drawable = getResources().getDrawable(R.drawable.worki_logo_200_200);

                byte[] byteArray = ImageUtils.toByteArray((BitmapDrawable) drawable);
                resume.setAvatar(byteArray);

                resumeDao.update(resume);
                this.finish();
            });
        } else {
            throw new ObjectNotFoundException("RESUME EDIT NOT FOUND!");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            ImageView imageView = findViewById(R.id.avatarEditImageView);
            try {
                Uri selectedImage = imageReturnedIntent.getData();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                imageView.setImageBitmap(ImageUtils.getResizedBitmap(bitmap, 200));
            } catch (IOException e) {
                Log.e(getClass().getSimpleName(), Objects.requireNonNull(e.getMessage()));
            }
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

}
