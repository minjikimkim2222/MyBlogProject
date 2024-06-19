package org.myblog.domain.tag.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.myblog.domain.post.domain.Post;

import java.util.List;

@Entity
@Table(name = "tags")
@Getter @Setter
@NoArgsConstructor
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tagName;

    @ManyToMany(mappedBy = "tags")
    private List<Post> posts;
}
