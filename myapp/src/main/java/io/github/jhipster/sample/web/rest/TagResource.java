package io.github.jhipster.sample.web.rest;

import static jakarta.ws.rs.core.UriBuilder.fromPath;

import io.github.jhipster.sample.domain.Tag;
import io.github.jhipster.sample.service.Paged;
import io.github.jhipster.sample.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.sample.web.rest.vm.PageRequestVM;
import io.github.jhipster.sample.web.rest.vm.SortRequestVM;
import io.github.jhipster.sample.web.util.HeaderUtil;
import io.github.jhipster.sample.web.util.PaginationUtil;
import io.github.jhipster.sample.web.util.ResponseUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.util.Optional;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * REST controller for managing {@link io.github.jhipster.sample.domain.Tag}.
 */
@Path("/api/tags")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class TagResource {

    private final Logger log = LoggerFactory.getLogger(TagResource.class);

    private static final String ENTITY_NAME = "tag";

    @ConfigProperty(name = "application.name")
    String applicationName;

    /**
     * {@code POST  /tags} : Create a new tag.
     *
     * @param tag the tag to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new tag, or with status {@code 400 (Bad Request)} if the tag has already an ID.
     */
    @POST
    @Transactional
    public Response createTag(@Valid Tag tag, @Context UriInfo uriInfo) {
        log.debug("REST request to save Tag : {}", tag);
        if (tag.id != null) {
            throw new BadRequestAlertException("A new tag cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = Tag.persistOrUpdate(tag);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /tags} : Updates an existing tag.
     *
     * @param tag the tag to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated tag,
     * or with status {@code 400 (Bad Request)} if the tag is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tag couldn't be updated.
     */
    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateTag(@Valid Tag tag, @PathParam("id") Long id) {
        log.debug("REST request to update Tag : {}", tag);
        if (tag.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = Tag.persistOrUpdate(tag);
        var response = Response.ok().entity(result);
        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tag.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code DELETE  /tags/:id} : delete the "id" tag.
     *
     * @param id the id of the tag to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteTag(@PathParam("id") Long id) {
        log.debug("REST request to delete Tag : {}", id);
        Tag.findByIdOptional(id).ifPresent(tag -> {
            tag.delete();
        });
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /tags} : get all the tags.
     *
     * @param pageRequest the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link Response} with status {@code 200 (OK)} and the list of tags in body.
     */
    @GET
    @Transactional
    public Response getAllTags(
        @BeanParam PageRequestVM pageRequest,
        @BeanParam SortRequestVM sortRequest,
        @Context UriInfo uriInfo,
        @QueryParam(value = "eagerload") boolean eagerload
    ) {
        log.debug("REST request to get a page of Tags");
        var page = pageRequest.toPage();
        var sort = sortRequest.toSort();
        Paged<Tag> result;
        if (eagerload) {
            var tags = Tag.findAllWithEagerRelationships().page(page).list();
            var totalCount = Tag.findAll().count();
            var pageCount = Tag.findAll().page(page).pageCount();
            result = new Paged<>(page.index, page.size, totalCount, pageCount, tags);
        } else {
            result = new Paged<>(Tag.findAll(sort).page(page));
        }
        var response = Response.ok().entity(result.content);
        response = PaginationUtil.withPaginationInfo(response, uriInfo, result);
        return response.build();
    }

    /**
     * {@code GET  /tags/:id} : get the "id" tag.
     *
     * @param id the id of the tag to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the tag, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")
    public Response getTag(@PathParam("id") Long id) {
        log.debug("REST request to get Tag : {}", id);
        Optional<Tag> tag = Tag.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(tag);
    }
}
