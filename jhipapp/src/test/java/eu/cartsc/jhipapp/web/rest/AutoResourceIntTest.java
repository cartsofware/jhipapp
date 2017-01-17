package eu.cartsc.jhipapp.web.rest;

import eu.cartsc.jhipapp.JhipappApp;

import eu.cartsc.jhipapp.domain.Auto;
import eu.cartsc.jhipapp.repository.AutoRepository;
import eu.cartsc.jhipapp.service.AutoService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static eu.cartsc.jhipapp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import eu.cartsc.jhipapp.domain.enumeration.Statoauto;
/**
 * Test class for the AutoResource REST controller.
 *
 * @see AutoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JhipappApp.class)
public class AutoResourceIntTest {

    private static final String DEFAULT_TARGA = "AAAAAAAAAA";
    private static final String UPDATED_TARGA = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATAINSERIMENTO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATAINSERIMENTO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Float DEFAULT_LAT = 1F;
    private static final Float UPDATED_LAT = 2F;

    private static final Float DEFAULT_LNG = 1F;
    private static final Float UPDATED_LNG = 2F;

    private static final Statoauto DEFAULT_STATO = Statoauto.libero;
    private static final Statoauto UPDATED_STATO = Statoauto.occupato;

    @Inject
    private AutoRepository autoRepository;

    @Inject
    private AutoService autoService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restAutoMockMvc;

    private Auto auto;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AutoResource autoResource = new AutoResource();
        ReflectionTestUtils.setField(autoResource, "autoService", autoService);
        this.restAutoMockMvc = MockMvcBuilders.standaloneSetup(autoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Auto createEntity(EntityManager em) {
        Auto auto = new Auto()
                .targa(DEFAULT_TARGA)
                .datainserimento(DEFAULT_DATAINSERIMENTO)
                .lat(DEFAULT_LAT)
                .lng(DEFAULT_LNG)
                .stato(DEFAULT_STATO);
        return auto;
    }

    @Before
    public void initTest() {
        auto = createEntity(em);
    }

    @Test
    @Transactional
    public void createAuto() throws Exception {
        int databaseSizeBeforeCreate = autoRepository.findAll().size();

        // Create the Auto

        restAutoMockMvc.perform(post("/api/autos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(auto)))
            .andExpect(status().isCreated());

        // Validate the Auto in the database
        List<Auto> autoList = autoRepository.findAll();
        assertThat(autoList).hasSize(databaseSizeBeforeCreate + 1);
        Auto testAuto = autoList.get(autoList.size() - 1);
        assertThat(testAuto.getTarga()).isEqualTo(DEFAULT_TARGA);
        assertThat(testAuto.getDatainserimento()).isEqualTo(DEFAULT_DATAINSERIMENTO);
        assertThat(testAuto.getLat()).isEqualTo(DEFAULT_LAT);
        assertThat(testAuto.getLng()).isEqualTo(DEFAULT_LNG);
        assertThat(testAuto.getStato()).isEqualTo(DEFAULT_STATO);
    }

    @Test
    @Transactional
    public void createAutoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = autoRepository.findAll().size();

        // Create the Auto with an existing ID
        Auto existingAuto = new Auto();
        existingAuto.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAutoMockMvc.perform(post("/api/autos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(existingAuto)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Auto> autoList = autoRepository.findAll();
        assertThat(autoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTargaIsRequired() throws Exception {
        int databaseSizeBeforeTest = autoRepository.findAll().size();
        // set the field null
        auto.setTarga(null);

        // Create the Auto, which fails.

        restAutoMockMvc.perform(post("/api/autos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(auto)))
            .andExpect(status().isBadRequest());

        List<Auto> autoList = autoRepository.findAll();
        assertThat(autoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDatainserimentoIsRequired() throws Exception {
        int databaseSizeBeforeTest = autoRepository.findAll().size();
        // set the field null
        auto.setDatainserimento(null);

        // Create the Auto, which fails.

        restAutoMockMvc.perform(post("/api/autos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(auto)))
            .andExpect(status().isBadRequest());

        List<Auto> autoList = autoRepository.findAll();
        assertThat(autoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAutos() throws Exception {
        // Initialize the database
        autoRepository.saveAndFlush(auto);

        // Get all the autoList
        restAutoMockMvc.perform(get("/api/autos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(auto.getId().intValue())))
            .andExpect(jsonPath("$.[*].targa").value(hasItem(DEFAULT_TARGA.toString())))
            .andExpect(jsonPath("$.[*].datainserimento").value(hasItem(sameInstant(DEFAULT_DATAINSERIMENTO))))
            .andExpect(jsonPath("$.[*].lat").value(hasItem(DEFAULT_LAT.doubleValue())))
            .andExpect(jsonPath("$.[*].lng").value(hasItem(DEFAULT_LNG.doubleValue())))
            .andExpect(jsonPath("$.[*].stato").value(hasItem(DEFAULT_STATO.toString())));
    }

    @Test
    @Transactional
    public void getAuto() throws Exception {
        // Initialize the database
        autoRepository.saveAndFlush(auto);

        // Get the auto
        restAutoMockMvc.perform(get("/api/autos/{id}", auto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(auto.getId().intValue()))
            .andExpect(jsonPath("$.targa").value(DEFAULT_TARGA.toString()))
            .andExpect(jsonPath("$.datainserimento").value(sameInstant(DEFAULT_DATAINSERIMENTO)))
            .andExpect(jsonPath("$.lat").value(DEFAULT_LAT.doubleValue()))
            .andExpect(jsonPath("$.lng").value(DEFAULT_LNG.doubleValue()))
            .andExpect(jsonPath("$.stato").value(DEFAULT_STATO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAuto() throws Exception {
        // Get the auto
        restAutoMockMvc.perform(get("/api/autos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAuto() throws Exception {
        // Initialize the database
        autoService.save(auto);

        int databaseSizeBeforeUpdate = autoRepository.findAll().size();

        // Update the auto
        Auto updatedAuto = autoRepository.findOne(auto.getId());
        updatedAuto
                .targa(UPDATED_TARGA)
                .datainserimento(UPDATED_DATAINSERIMENTO)
                .lat(UPDATED_LAT)
                .lng(UPDATED_LNG)
                .stato(UPDATED_STATO);

        restAutoMockMvc.perform(put("/api/autos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAuto)))
            .andExpect(status().isOk());

        // Validate the Auto in the database
        List<Auto> autoList = autoRepository.findAll();
        assertThat(autoList).hasSize(databaseSizeBeforeUpdate);
        Auto testAuto = autoList.get(autoList.size() - 1);
        assertThat(testAuto.getTarga()).isEqualTo(UPDATED_TARGA);
        assertThat(testAuto.getDatainserimento()).isEqualTo(UPDATED_DATAINSERIMENTO);
        assertThat(testAuto.getLat()).isEqualTo(UPDATED_LAT);
        assertThat(testAuto.getLng()).isEqualTo(UPDATED_LNG);
        assertThat(testAuto.getStato()).isEqualTo(UPDATED_STATO);
    }

    @Test
    @Transactional
    public void updateNonExistingAuto() throws Exception {
        int databaseSizeBeforeUpdate = autoRepository.findAll().size();

        // Create the Auto

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAutoMockMvc.perform(put("/api/autos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(auto)))
            .andExpect(status().isCreated());

        // Validate the Auto in the database
        List<Auto> autoList = autoRepository.findAll();
        assertThat(autoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAuto() throws Exception {
        // Initialize the database
        autoService.save(auto);

        int databaseSizeBeforeDelete = autoRepository.findAll().size();

        // Get the auto
        restAutoMockMvc.perform(delete("/api/autos/{id}", auto.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Auto> autoList = autoRepository.findAll();
        assertThat(autoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
