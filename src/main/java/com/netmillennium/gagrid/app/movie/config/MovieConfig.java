package com.netmillennium.gagrid.app.movie.config;

import java.util.ArrayList;
import java.util.List;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteLogger;
import org.apache.ignite.resources.LoggerResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;

import com.netmillennium.gagrid.model.Gene;
import com.netmillennium.gagrid.parameter.GAConfiguration;
import com.netmillennium.gagrid.parameter.GAGrid;
import com.netmillennium.gagrid.parameter.GAGridConstants;
import com.netmillennium.gagrid.services.movie.Movie;
import com.netmillennium.gagrid.services.movie.MovieFitnessFunction;
import com.netmillennium.gagrid.services.movie.MovieService;
import com.netmillennium.gagrid.services.movie.MovieTerminateCriteria;

@Configuration
public class MovieConfig {

	
	@Autowired
	private Ignite ignite;
	
	/** Ignite logger */
    @LoggerResource
	private IgniteLogger logger;
	
	@Autowired
    private ApplicationContext applicationContext;

	@Autowired
	@Qualifier("movieService")
    private MovieService movieService;
	
	@Autowired
	private Environment environment;
	
	@Value("${GENRE:Romance}")
	private List<String> genres;
	
	@Bean ("gaConfiguration")
	//DependsOn("movieService")
	public GAConfiguration gaConfiguration()
	{
        // Create GAConfiguration
        GAConfiguration gaConfig = new GAConfiguration();
        
        System.out.println("genres " + genres.toString());

        // Define Chromosome
        gaConfig.setChromosomeLen(3);
        gaConfig.setPopulationSize(1000);
        gaConfig.setGenePool(getGenePool());
        gaConfig.setTruncateRate(.10);
        gaConfig.setCrossOverRate(.50);
        gaConfig.setMutationRate(.50);
      
        gaConfig.setSelectionMtd(GAGridConstants.SELECTION_METHOD.SELECTION_METHOD_TRUNCATION);
      
     
        //Create fitness function
        MovieFitnessFunction function = new MovieFitnessFunction(genres);
        
        //set fitness function
        gaConfig.setFitnessFunction(function);
        
        MovieTerminateCriteria termCriteria = new MovieTerminateCriteria(ignite);
        
        gaConfig.setTerminateCriteria(termCriteria);


        return gaConfig;
	}
	
	@Bean("gaGrid")
	@DependsOn("gaConfiguration")
	public GAGrid gagrid()
	{
		GAGrid gaGrid = new GAGrid((GAConfiguration)applicationContext.getBean("gaConfiguration"), ignite);
		return gaGrid;
	}
	 /**
     * Helper routine to initialize Gene pool
     * 
     * In typical usecase genes may be stored in database.
     * 
     * @return List<Gene>
     */
    private List<Gene> getGenePool() {
     
    	List<Movie> movies = movieService.getAllMovies();
    	List<Gene> genes = new ArrayList();
    	
    	for (Movie amovie: movies)
    	{
    		genes.add(new Gene(amovie));
    	}
        return genes;
    }
    

}
