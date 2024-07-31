package io.github.jhipster.sample.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.hibernate.annotations.Type;

/**
 * A Post.
 */
@Entity
@Table(name = "post")
@RegisterForReflection
public class Post extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    public Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    public String title;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "content", nullable = false)
    public String content;

    @NotNull
    @Column(name = "date", nullable = false)
    public Instant date;

    @ManyToOne
    @JoinColumn(name = "blog_id")
    public Blog blog;

    @ManyToMany
    @JoinTable(
        name = "rel_post__tag",
        joinColumns = @JoinColumn(name = "post_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id")
    )
    @JsonbTransient
    public Set<Tag> tags = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Post)) {
            return false;
        }
        return id != null && id.equals(((Post) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Post{" + "id=" + id + ", title='" + title + "'" + ", content='" + content + "'" + ", date='" + date + "'" + "}";
    }

    public Post update() {
        return update(this);
    }

    public Post persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static Post update(Post post) {
        if (post == null) {
            throw new IllegalArgumentException("post can't be null");
        }
        var entity = Post.<Post>findById(post.id);
        if (entity != null) {
            entity.title = post.title;
            entity.content = post.content;
            entity.date = post.date;
            entity.blog = post.blog;
            entity.tags = post.tags;
        }
        return entity;
    }

    public static Post persistOrUpdate(Post post) {
        if (post == null) {
            throw new IllegalArgumentException("post can't be null");
        }
        if (post.id == null) {
            persist(post);
            return post;
        } else {
            return update(post);
        }
    }

    public static PanacheQuery<Post> findAllWithEagerRelationships() {
        return find("select distinct post from Post post left join fetch post.tags");
    }

    public static Optional<Post> findOneWithEagerRelationships(Long id) {
        return find("select post from Post post left join fetch post.tags where post.id =?1", id).firstResultOptional();
    }
}
