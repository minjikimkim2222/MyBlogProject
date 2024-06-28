package org.myblog.domain.tag.repository;

import org.myblog.domain.tag.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByTagName(String tagName);

    void deleteByTagName(String tagName);
}
