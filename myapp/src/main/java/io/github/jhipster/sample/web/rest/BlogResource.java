package io.github.jhipster.sample.web.rest;

import static jakarta.ws.rs.core.UriBuilder.fromPath;

import io.github.jhipster.sample.domain.Blog;
import io.github.jhipster.sample.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.sample.web.util.HeaderUtil;
import io.github.jhipster.sample.web.util.ResponseUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.util.List;
import java.util.Optional;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * REST controller for managing {@link io.github.jhipster.sample.domain.Blog}.
 */
@Path("/api/blogs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class BlogResource {

    private final Logger log = LoggerFactory.getLogger(BlogResource.class);

    private static final String ENTITY_NAME = "blog";

    @ConfigProperty(name = "application.name")
    String applicationName;

    /**
     * {@code POST  /blogs} : Create a new blog.
     *
     * @param blog the blog to create.
     * @return the {@link Response} with status {@code 201 (Created)} and with body the new blog, or with status {@code 400 (Bad Request)} if the blog has already an ID.
     */
    @POST
    @Transactional
    public Response createBlog(@Valid Blog blog, @Context UriInfo uriInfo) {
        log.debug("REST request to save Blog : {}", blog);
        if (blog.id != null) {
            throw new BadRequestAlertException("A new blog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        var result = Blog.persistOrUpdate(blog);
        var response = Response.created(fromPath(uriInfo.getPath()).path(result.id.toString()).build()).entity(result);
        HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code PUT  /blogs} : Updates an existing blog.
     *
     * @param blog the blog to update.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the updated blog,
     * or with status {@code 400 (Bad Request)} if the blog is not valid,
     * or with status {@code 500 (Internal Server Error)} if the blog couldn't be updated.
     */
    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateBlog(@Valid Blog blog, @PathParam("id") Long id) {
        log.debug("REST request to update Blog : {}", blog);
        if (blog.id == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        var result = Blog.persistOrUpdate(blog);
        var response = Response.ok().entity(result);
        HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, blog.id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code DELETE  /blogs/:id} : delete the "id" blog.
     *
     * @param id the id of the blog to delete.
     * @return the {@link Response} with status {@code 204 (NO_CONTENT)}.
     */
    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteBlog(@PathParam("id") Long id) {
        log.debug("REST request to delete Blog : {}", id);
        Blog.findByIdOptional(id).ifPresent(blog -> {
            blog.delete();
        });
        var response = Response.noContent();
        HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()).forEach(response::header);
        return response.build();
    }

    /**
     * {@code GET  /blogs} : get all the blogs.
     *     * @return the {@link Response} with status {@code 200 (OK)} and the list of blogs in body.
     */
    @GET
    public List<Blog> getAllBlogs() {
        log.debug("REST request to get all Blogs");
        return Blog.findAll().list();
    }

    /**
     * {@code GET  /blogs/:id} : get the "id" blog.
     *
     * @param id the id of the blog to retrieve.
     * @return the {@link Response} with status {@code 200 (OK)} and with body the blog, or with status {@code 404 (Not Found)}.
     */
    @GET
    @Path("/{id}")
    public Response getBlog(@PathParam("id") Long id) {
        log.debug("REST request to get Blog : {}", id);
        Optional<Blog> blog = Blog.findByIdOptional(id);
        return ResponseUtil.wrapOrNotFound(blog);
    }
}
