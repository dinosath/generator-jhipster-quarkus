package io.github.jhipster.sample.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * A Tag.
 */
@Entity
@Table(name = "tag")
@RegisterForReflection
public class Tag extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    public Long id;

    @NotNull
    @Size(min = 2)
    @Column(name = "name", nullable = false)
    public String name;

    @ManyToMany(mappedBy = "tags")
    @JsonbTransient
    public Set<Post> entries = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tag)) {
            return false;
        }
        return id != null && id.equals(((Tag) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Tag{" + "id=" + id + ", name='" + name + "'" + "}";
    }

    public Tag update() {
        return update(this);
    }

    public Tag persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static Tag update(Tag tag) {
        if (tag == null) {
            throw new IllegalArgumentException("tag can't be null");
        }
        var entity = Tag.<Tag>findById(tag.id);
        if (entity != null) {
            entity.name = tag.name;
            entity.entries = tag.entries;
        }
        return entity;
    }

    public static Tag persistOrUpdate(Tag tag) {
        if (tag == null) {
            throw new IllegalArgumentException("tag can't be null");
        }
        if (tag.id == null) {
            persist(tag);
            return tag;
        } else {
            return update(tag);
        }
    }

    public static PanacheQuery<Tag> findAllWithEagerRelationships() {
        return find("select distinct tag from Tag tag");
    }

    public static Optional<Tag> findOneWithEagerRelationships(Long id) {
        return find("select tag from Tag tag where tag.id =?1", id).firstResultOptional();
    }
}
