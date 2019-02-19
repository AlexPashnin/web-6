package ru.ifmo.web.service;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.ifmo.web.database.dao.AstartesDAO;
import ru.ifmo.web.database.entity.Astartes;
import ru.ifmo.web.service.exceptions.IdNotFoundException;
import ru.ifmo.web.service.exceptions.InternalException;
import ru.ifmo.web.standalone.App;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

@Data
@Slf4j
@Path("/astartes")
@Produces({MediaType.APPLICATION_JSON})
public class AstartesService {
    private AstartesDAO astartesDAO;

    public AstartesService() throws IOException {
        log.info("Creating service");
        InputStream dsPropsStream = App.class.getClassLoader().getResourceAsStream("datasource.properties");
        Properties dsProps = new Properties();
        dsProps.load(dsPropsStream);
        HikariConfig hikariConfig = new HikariConfig(dsProps);
        HikariDataSource dataSource = new HikariDataSource(hikariConfig);
        this.astartesDAO = new AstartesDAO(dataSource);
    }

    @GET
    @Path("/all")
    public List<Astartes> findAll() throws InternalException {
        try {
            return astartesDAO.findAll();
        } catch (SQLException e) {
            String message = "Произошла Внутренняя ошибка сервера: SQL exception: " + e.getMessage() + ". State: " + e.getSQLState();
            throw new InternalException(message);
        }
    }

    @GET
    @Path("/filter")
    public List<Astartes> findWithFilters(@QueryParam("id") Long id, @QueryParam("name") String name,
                                          @QueryParam("title") String title, @QueryParam("position") String position,
                                          @QueryParam("planet") String planet, @QueryParam("birthdate") String birthdate) throws InternalException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        if (birthdate != null) {
            try {
                date = sdf.parse(birthdate);
            } catch (ParseException ignored) {
            }
        }
        try {
            return astartesDAO.findWithFilters(id, name, title, position, planet, date);
        } catch (SQLException e) {
            String message = "Произошла Внутренняя ошибка сервера: SQL exception: " + e.getMessage() + ". State: " + e.getSQLState();
            throw new InternalException(message);
        }
    }

    @PUT
    @Path("/update")
    public String update(@QueryParam("id") Long id, @QueryParam("name") String name,
                         @QueryParam("title") String title, @QueryParam("position") String position,
                         @QueryParam("planet") String planet, @QueryParam("birthdate") String birthdate) throws IdNotFoundException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        if (birthdate != null) {
            try {
                date = sdf.parse(birthdate);
            } catch (ParseException ignored) {
            }
        }
        try {
            int count = astartesDAO.update(id, name, title, position, planet, date);
            if (count == 0) {
                throw new IdNotFoundException("Запись с id=" + id + " не существует");
            }
            return count + "";
        } catch (SQLException e) {
            String message = "Произошла Внутренняя ошибка сервера: SQL exception: " + e.getMessage() + ". State: " + e.getSQLState();
            throw new InternalException(message);
        }
    }

    @DELETE
    @Path("/delete")
    public String delete(@QueryParam("id") Long id) throws IdNotFoundException {
        log.info("DELETE");
        try {
            int count = astartesDAO.delete(id);
            if (count == 0) {
                throw new IdNotFoundException("Запись с id=" + id + " не существует");
            }
            return count + "";
        } catch (SQLException e) {
            String message = "Произошла Внутренняя ошибка сервера: SQL exception: " + e.getMessage() + ". State: " + e.getSQLState();
            throw new InternalException(message);
        }
    }

    @POST
    @Path("/create")
    public String create(@QueryParam("name") String name,
                         @QueryParam("title") String title, @QueryParam("position") String position,
                         @QueryParam("planet") String planet, @QueryParam("birthdate") String birthdate) throws InternalException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        if (birthdate != null) {
            try {
                date = sdf.parse(birthdate);
            } catch (ParseException ignored) {
            }
        }

        try {
            return astartesDAO.create(name, title, position, planet, date) + "";
        } catch (SQLException e) {
            String message = "Произошла Внутренняя ошибка сервера: SQL exception: " + e.getMessage() + ". State: " + e.getSQLState();
            throw new InternalException(message);
        }
    }
}
