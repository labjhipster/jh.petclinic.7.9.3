package org.jhipster.petclinic.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.jhipster.petclinic.IntegrationTest;
import org.jhipster.petclinic.domain.Specialty;
import org.jhipster.petclinic.domain.Vet;
import org.jhipster.petclinic.repository.VetRepository;
import org.jhipster.petclinic.service.VetService;
import org.jhipster.petclinic.service.criteria.VetCriteria;
import org.jhipster.petclinic.service.dto.VetDTO;
import org.jhipster.petclinic.service.mapper.VetMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link VetResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class VetResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/vets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VetRepository vetRepository;

    @Mock
    private VetRepository vetRepositoryMock;

    @Autowired
    private VetMapper vetMapper;

    @Mock
    private VetService vetServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVetMockMvc;

    private Vet vet;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vet createEntity(EntityManager em) {
        Vet vet = new Vet().firstName(DEFAULT_FIRST_NAME).lastName(DEFAULT_LAST_NAME);
        return vet;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vet createUpdatedEntity(EntityManager em) {
        Vet vet = new Vet().firstName(UPDATED_FIRST_NAME).lastName(UPDATED_LAST_NAME);
        return vet;
    }

    @BeforeEach
    public void initTest() {
        vet = createEntity(em);
    }

    @Test
    @Transactional
    void createVet() throws Exception {
        int databaseSizeBeforeCreate = vetRepository.findAll().size();
        // Create the Vet
        VetDTO vetDTO = vetMapper.toDto(vet);
        restVetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vetDTO)))
            .andExpect(status().isCreated());

        // Validate the Vet in the database
        List<Vet> vetList = vetRepository.findAll();
        assertThat(vetList).hasSize(databaseSizeBeforeCreate + 1);
        Vet testVet = vetList.get(vetList.size() - 1);
        assertThat(testVet.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testVet.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
    }

    @Test
    @Transactional
    void createVetWithExistingId() throws Exception {
        // Create the Vet with an existing ID
        vet.setId(1L);
        VetDTO vetDTO = vetMapper.toDto(vet);

        int databaseSizeBeforeCreate = vetRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vetDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Vet in the database
        List<Vet> vetList = vetRepository.findAll();
        assertThat(vetList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = vetRepository.findAll().size();
        // set the field null
        vet.setFirstName(null);

        // Create the Vet, which fails.
        VetDTO vetDTO = vetMapper.toDto(vet);

        restVetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vetDTO)))
            .andExpect(status().isBadRequest());

        List<Vet> vetList = vetRepository.findAll();
        assertThat(vetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = vetRepository.findAll().size();
        // set the field null
        vet.setLastName(null);

        // Create the Vet, which fails.
        VetDTO vetDTO = vetMapper.toDto(vet);

        restVetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vetDTO)))
            .andExpect(status().isBadRequest());

        List<Vet> vetList = vetRepository.findAll();
        assertThat(vetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVets() throws Exception {
        // Initialize the database
        vetRepository.saveAndFlush(vet);

        // Get all the vetList
        restVetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vet.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVetsWithEagerRelationshipsIsEnabled() throws Exception {
        when(vetServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restVetMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(vetServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVetsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(vetServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restVetMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(vetRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getVet() throws Exception {
        // Initialize the database
        vetRepository.saveAndFlush(vet);

        // Get the vet
        restVetMockMvc
            .perform(get(ENTITY_API_URL_ID, vet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vet.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME));
    }

    @Test
    @Transactional
    void getVetsByIdFiltering() throws Exception {
        // Initialize the database
        vetRepository.saveAndFlush(vet);

        Long id = vet.getId();

        defaultVetShouldBeFound("id.equals=" + id);
        defaultVetShouldNotBeFound("id.notEquals=" + id);

        defaultVetShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultVetShouldNotBeFound("id.greaterThan=" + id);

        defaultVetShouldBeFound("id.lessThanOrEqual=" + id);
        defaultVetShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllVetsByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        vetRepository.saveAndFlush(vet);

        // Get all the vetList where firstName equals to DEFAULT_FIRST_NAME
        defaultVetShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the vetList where firstName equals to UPDATED_FIRST_NAME
        defaultVetShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllVetsByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        vetRepository.saveAndFlush(vet);

        // Get all the vetList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultVetShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the vetList where firstName equals to UPDATED_FIRST_NAME
        defaultVetShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllVetsByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        vetRepository.saveAndFlush(vet);

        // Get all the vetList where firstName is not null
        defaultVetShouldBeFound("firstName.specified=true");

        // Get all the vetList where firstName is null
        defaultVetShouldNotBeFound("firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllVetsByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        vetRepository.saveAndFlush(vet);

        // Get all the vetList where firstName contains DEFAULT_FIRST_NAME
        defaultVetShouldBeFound("firstName.contains=" + DEFAULT_FIRST_NAME);

        // Get all the vetList where firstName contains UPDATED_FIRST_NAME
        defaultVetShouldNotBeFound("firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllVetsByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        vetRepository.saveAndFlush(vet);

        // Get all the vetList where firstName does not contain DEFAULT_FIRST_NAME
        defaultVetShouldNotBeFound("firstName.doesNotContain=" + DEFAULT_FIRST_NAME);

        // Get all the vetList where firstName does not contain UPDATED_FIRST_NAME
        defaultVetShouldBeFound("firstName.doesNotContain=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllVetsByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        vetRepository.saveAndFlush(vet);

        // Get all the vetList where lastName equals to DEFAULT_LAST_NAME
        defaultVetShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the vetList where lastName equals to UPDATED_LAST_NAME
        defaultVetShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllVetsByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        vetRepository.saveAndFlush(vet);

        // Get all the vetList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultVetShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the vetList where lastName equals to UPDATED_LAST_NAME
        defaultVetShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllVetsByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        vetRepository.saveAndFlush(vet);

        // Get all the vetList where lastName is not null
        defaultVetShouldBeFound("lastName.specified=true");

        // Get all the vetList where lastName is null
        defaultVetShouldNotBeFound("lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllVetsByLastNameContainsSomething() throws Exception {
        // Initialize the database
        vetRepository.saveAndFlush(vet);

        // Get all the vetList where lastName contains DEFAULT_LAST_NAME
        defaultVetShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the vetList where lastName contains UPDATED_LAST_NAME
        defaultVetShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllVetsByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        vetRepository.saveAndFlush(vet);

        // Get all the vetList where lastName does not contain DEFAULT_LAST_NAME
        defaultVetShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the vetList where lastName does not contain UPDATED_LAST_NAME
        defaultVetShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllVetsBySpecialtiesIsEqualToSomething() throws Exception {
        Specialty specialties;
        if (TestUtil.findAll(em, Specialty.class).isEmpty()) {
            vetRepository.saveAndFlush(vet);
            specialties = SpecialtyResourceIT.createEntity(em);
        } else {
            specialties = TestUtil.findAll(em, Specialty.class).get(0);
        }
        em.persist(specialties);
        em.flush();
        vet.addSpecialties(specialties);
        vetRepository.saveAndFlush(vet);
        Long specialtiesId = specialties.getId();

        // Get all the vetList where specialties equals to specialtiesId
        defaultVetShouldBeFound("specialtiesId.equals=" + specialtiesId);

        // Get all the vetList where specialties equals to (specialtiesId + 1)
        defaultVetShouldNotBeFound("specialtiesId.equals=" + (specialtiesId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultVetShouldBeFound(String filter) throws Exception {
        restVetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vet.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)));

        // Check, that the count call also returns 1
        restVetMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultVetShouldNotBeFound(String filter) throws Exception {
        restVetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restVetMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingVet() throws Exception {
        // Get the vet
        restVetMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVet() throws Exception {
        // Initialize the database
        vetRepository.saveAndFlush(vet);

        int databaseSizeBeforeUpdate = vetRepository.findAll().size();

        // Update the vet
        Vet updatedVet = vetRepository.findById(vet.getId()).get();
        // Disconnect from session so that the updates on updatedVet are not directly saved in db
        em.detach(updatedVet);
        updatedVet.firstName(UPDATED_FIRST_NAME).lastName(UPDATED_LAST_NAME);
        VetDTO vetDTO = vetMapper.toDto(updatedVet);

        restVetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vetDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vetDTO))
            )
            .andExpect(status().isOk());

        // Validate the Vet in the database
        List<Vet> vetList = vetRepository.findAll();
        assertThat(vetList).hasSize(databaseSizeBeforeUpdate);
        Vet testVet = vetList.get(vetList.size() - 1);
        assertThat(testVet.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testVet.getLastName()).isEqualTo(UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void putNonExistingVet() throws Exception {
        int databaseSizeBeforeUpdate = vetRepository.findAll().size();
        vet.setId(count.incrementAndGet());

        // Create the Vet
        VetDTO vetDTO = vetMapper.toDto(vet);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vetDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vet in the database
        List<Vet> vetList = vetRepository.findAll();
        assertThat(vetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVet() throws Exception {
        int databaseSizeBeforeUpdate = vetRepository.findAll().size();
        vet.setId(count.incrementAndGet());

        // Create the Vet
        VetDTO vetDTO = vetMapper.toDto(vet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vet in the database
        List<Vet> vetList = vetRepository.findAll();
        assertThat(vetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVet() throws Exception {
        int databaseSizeBeforeUpdate = vetRepository.findAll().size();
        vet.setId(count.incrementAndGet());

        // Create the Vet
        VetDTO vetDTO = vetMapper.toDto(vet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVetMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vetDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vet in the database
        List<Vet> vetList = vetRepository.findAll();
        assertThat(vetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVetWithPatch() throws Exception {
        // Initialize the database
        vetRepository.saveAndFlush(vet);

        int databaseSizeBeforeUpdate = vetRepository.findAll().size();

        // Update the vet using partial update
        Vet partialUpdatedVet = new Vet();
        partialUpdatedVet.setId(vet.getId());

        partialUpdatedVet.lastName(UPDATED_LAST_NAME);

        restVetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVet))
            )
            .andExpect(status().isOk());

        // Validate the Vet in the database
        List<Vet> vetList = vetRepository.findAll();
        assertThat(vetList).hasSize(databaseSizeBeforeUpdate);
        Vet testVet = vetList.get(vetList.size() - 1);
        assertThat(testVet.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testVet.getLastName()).isEqualTo(UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void fullUpdateVetWithPatch() throws Exception {
        // Initialize the database
        vetRepository.saveAndFlush(vet);

        int databaseSizeBeforeUpdate = vetRepository.findAll().size();

        // Update the vet using partial update
        Vet partialUpdatedVet = new Vet();
        partialUpdatedVet.setId(vet.getId());

        partialUpdatedVet.firstName(UPDATED_FIRST_NAME).lastName(UPDATED_LAST_NAME);

        restVetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVet))
            )
            .andExpect(status().isOk());

        // Validate the Vet in the database
        List<Vet> vetList = vetRepository.findAll();
        assertThat(vetList).hasSize(databaseSizeBeforeUpdate);
        Vet testVet = vetList.get(vetList.size() - 1);
        assertThat(testVet.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testVet.getLastName()).isEqualTo(UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingVet() throws Exception {
        int databaseSizeBeforeUpdate = vetRepository.findAll().size();
        vet.setId(count.incrementAndGet());

        // Create the Vet
        VetDTO vetDTO = vetMapper.toDto(vet);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vetDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vet in the database
        List<Vet> vetList = vetRepository.findAll();
        assertThat(vetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVet() throws Exception {
        int databaseSizeBeforeUpdate = vetRepository.findAll().size();
        vet.setId(count.incrementAndGet());

        // Create the Vet
        VetDTO vetDTO = vetMapper.toDto(vet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vet in the database
        List<Vet> vetList = vetRepository.findAll();
        assertThat(vetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVet() throws Exception {
        int databaseSizeBeforeUpdate = vetRepository.findAll().size();
        vet.setId(count.incrementAndGet());

        // Create the Vet
        VetDTO vetDTO = vetMapper.toDto(vet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVetMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(vetDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vet in the database
        List<Vet> vetList = vetRepository.findAll();
        assertThat(vetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVet() throws Exception {
        // Initialize the database
        vetRepository.saveAndFlush(vet);

        int databaseSizeBeforeDelete = vetRepository.findAll().size();

        // Delete the vet
        restVetMockMvc.perform(delete(ENTITY_API_URL_ID, vet.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Vet> vetList = vetRepository.findAll();
        assertThat(vetList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
