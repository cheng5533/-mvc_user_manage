package com.cheng.ming.mvc_user_manage.service;

import com.cheng.ming.mvc_user_manage.entity.Tag;
import com.cheng.ming.mvc_user_manage.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 标签业务逻辑层
 */
@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    public List<Tag> findAll() {
        return tagRepository.findAll();
    }

    public Optional<Tag> findById(Long id) {
        return tagRepository.findById(id);
    }

    @Transactional
    public Tag save(Tag tag) {
        return tagRepository.save(tag);
    }

    @Transactional
    public void deleteById(Long id) {
        tagRepository.deleteById(id);
    }
}
