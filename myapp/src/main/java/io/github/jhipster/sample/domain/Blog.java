package io.github.jhipster.sample.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;

/**
 * A Blog.
 */
@Entity
@Table(name = "blog")
@RegisterForReflection
public class Blog extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    public Long id;

    @NotNull
    @Size(min = 3)
    @Column(name = "name", nullable = false)
    public String name;

    @NotNull
    @Size(min = 2)
    @Column(name = "handle", nullable = false)
    public String handle;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Blog)) {
            return false;
        }
        return id != null && id.equals(((Blog) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Blog{" + "id=" + id + ", name='" + name + "'" + ", handle='" + handle + "'" + "}";
    }

    public Blog update() {
        return update(this);
    }

    public Blog persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static Blog update(Blog blog) {
        if (blog == null) {
            throw new IllegalArgumentException("blog can't be null");
        }
        var entity = Blog.<Blog>findById(blog.id);
        if (entity != null) {
            entity.name = blog.name;
            entity.handle = blog.handle;
        }
        return entity;
    }

    public static Blog persistOrUpdate(Blog blog) {
        if (blog == null) {
            throw new IllegalArgumentException("blog can't be null");
        }
        if (blog.id == null) {
            persist(blog);
            return blog;
        } else {
            return update(blog);
        }
    }
}
