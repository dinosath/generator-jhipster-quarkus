package io.github.jhipster.sample.web.rest;

import static jakarta.ws.rs.core.UriBuilder.fromPath;

import io.github.jhipster.sample.domain.Post;
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
 * REST controller for managing {@link io.github.jhipster.sample.domain.Post}.
 */
@Path("/api/posts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class PostResource {

    private final Logger log = LoggerFactory.getLogger(PostResource.class);

    private static final String ENTITY_NAME = "post";

    @ConfigProperty(name = "application.name")
    String applicationName;

    /**
     * {@code POST  /posts} : Create a new post.
     *
     * @param post the post to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new post, or with status {@code 400 (Bad Request)} if the post has already an ID.
     */
    @POST
    @Transactional
    public Response createPost(@Valid Post post, @Context UriInfo uriInfo) {
        log.debug("REST request to save Post : {}", post);
        if (post.id != null) {
            throw new BadRequestAlertException("A new post cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = Post.persistOrUpdate(post);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /posts} : Updates an existing post.
     *
     * @param post the post to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated post,
     * or with status {@code 400 (Bad Request)} if the post is not valid,
     * or with status {@code 500 (Internal Server Error)} if the post couldn't be updated.
     */
    @PUT
    @Path("/{id}")
    @Transactional
    public Response updatePost(@Valid Post post, @PathParam("id") Long id) {
        log.debug("REST request to update Post : {}", post);
        if (post.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = Post.persistOrUpdate(post);
        var response = Response.ok().entity(result);
        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, post.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code DELETE  /posts/:id} : delete the "id" post.
     *
     * @param id the id of the post to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deletePost(@PathParam("id") Long id) {
        log.debug("REST request to delete Post : {}", id);
        Post.findByIdOptional(id).ifPresent(post -> {
            post.delete();
        });
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /posts} : get all the posts.
     *
     * @param pageRequest the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link Response} with status {@code 200 (OK)} and the list of posts in body.
     */
    @GET
    @Transactional
    public Response getAllPosts(
        @BeanParam PageRequestVM pageRequest,
        @BeanParam SortRequestVM sortRequest,
        @Context UriInfo uriInfo,
        @QueryParam(value = "eagerload") boolean eagerload
    ) {
        log.debug("REST request to get a page of Posts");
        var page = pageRequest.toPage();
        var sort = sortRequest.toSort();
        Paged<Post> result;
        if (eagerload) {
            var posts = Post.findAllWithEagerRelationships().page(page).list();
            var totalCount = Post.findAll().count();
            var pageCount = Post.findAll().page(page).pageCount();
            result = new Paged<>(page.index, page.size, totalCount, pageCount, posts);
        } else {
            result = new Paged<>(Post.findAll(sort).page(page));
        }
        var response = Response.ok().entity(result.content);
        response = PaginationUtil.withPaginationInfo(response, uriInfo, result);
        return response.build();
    }

    /**
     * {@code GET  /posts/:id} : get the "id" post.
     *
     * @param id the id of the post to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the post, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")
    public Response getPost(@PathParam("id") Long id) {
        log.debug("REST request to get Post : {}", id);
        Optional<Post> post = Post.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(post);
    }
}
