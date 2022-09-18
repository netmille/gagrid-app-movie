package com.netmillennium.gagrid.app.movie.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.jdbc.core.JdbcTemplate;

import com.netmillennium.gagrid.services.movie.MovieService;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class PostgresDSMovieConfig {

	@Bean
	@ConfigurationProperties(prefix = "spring.ds.pgsql")
	public DataSourceProperties pgDataSourceProperties() {
		return new DataSourceProperties();
	}

	@Bean("pgDataSource")
	public DataSource pgDataSource() {
		return pgDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
	}

	@Bean("pgJdbcTemplate")
	@DependsOn("pgDataSource")
	public JdbcTemplate pgJdbcTemplate(@Qualifier("pgDataSource") DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

	@Bean("movieService")
	@DependsOn("pgJdbcTemplate")
	public MovieService movieService(@Qualifier("pgJdbcTemplate") JdbcTemplate jdbcTemplate) {
		MovieService service = new MovieService(jdbcTemplate);
		return service;
	}

}