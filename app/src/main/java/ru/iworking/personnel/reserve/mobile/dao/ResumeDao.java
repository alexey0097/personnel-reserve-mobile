package ru.iworking.personnel.reserve.mobile.dao;

import com.google.common.base.Optional;
import ru.iworking.personnel.reserve.mobile.entity.Resume;

import java.util.List;

public interface ResumeDao {

    Optional<Resume> findById(Long id);
    List<Resume> findAll();
    Optional<Resume>  create(Resume obj);
    Optional<Resume>  update(Resume obj);
    void deleteById(Long id);

}
