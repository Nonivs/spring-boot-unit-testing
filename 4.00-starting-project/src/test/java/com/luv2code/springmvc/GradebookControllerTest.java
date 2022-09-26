package com.luv2code.springmvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.models.Grade;
import com.luv2code.springmvc.repository.HistoryGradesDao;
import com.luv2code.springmvc.repository.MathGradesDao;
import com.luv2code.springmvc.repository.ScienceGradesDao;
import com.luv2code.springmvc.repository.StudentDao;
import com.luv2code.springmvc.service.StudentAndGradeService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
public class GradebookControllerTest {

    private static MockHttpServletRequest request;

    @PersistenceContext
    private EntityManager entityManager;

    @Mock
    StudentAndGradeService studentCreateServiceMock;

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private StudentDao studentDao;

    @Autowired
    private MathGradesDao mathGradeDao;

    @Autowired
    private ScienceGradesDao scienceGradeDao;

    @Autowired
    private HistoryGradesDao historyGradeDao;

    @Autowired
    private StudentAndGradeService studentService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private CollegeStudent student;

    @Value("${sql.script.create.student}")
    private String sqlAddStudent;

    @Value("${sql.script.create.math.grade}")
    private String sqlAddMathGrade;

    @Value("${sql.script.create.science.grade}")
    private String sqlAddScienceGrade;

    @Value("${sql.script.create.history.grade}")
    private String sqlAddHistoryGrade;

    @Value("${sql.script.delete.student}")
    private String sqlDeleteStudent;

    @Value("${sql.script.delete.math.grade}")
    private String sqlDeleteMathGrade;

    @Value("${sql.script.delete.science.grade}")
    private String sqlDeleteScienceGrade;

    @Value("${sql.script.delete.history.grade}")
    private String sqlDeleteHistoryGrade;

    public static final MediaType APPLICATION_JSON_UTF8 = MediaType.APPLICATION_JSON;


    @BeforeAll
    public static void setup() {

        request = new MockHttpServletRequest();

        request.setParameter("firstname", "Chad");

        request.setParameter("lastname", "Darby");

        request.setParameter("emailAddress", "chad.darby@luv2code_school.com");
    }

    @BeforeEach
    public void setupDatabase() {
        jdbc.execute(sqlAddStudent);
        jdbc.execute(sqlAddMathGrade);
        jdbc.execute(sqlAddScienceGrade);
        jdbc.execute(sqlAddHistoryGrade);
    }

    @Test
    public void getStudentsHttpRequest() throws Exception {

        student.setFirstname("Chad");
        student.setLastname("Darby");
        student.setEmailAddress("chad.darby@luv2code_school.com");
        entityManager.persist(student);
        entityManager.flush();

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)));

    }

    @Test
    public void createStudentHttpRequest() throws Exception {
        student.setFirstname("Chad");
        student.setLastname("Darby");
        student.setEmailAddress("chad_darby@luv2code_school.com");

        mockMvc.perform(post("/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        CollegeStudent verifyStudent = studentDao.findByEmailAddress("chad_darby@luv2code_school.com");
        assertNotNull(verifyStudent, "Student should be valid.");

    }

    @Test
    void deleteStudentHttpRequest() throws Exception {

        assertTrue(studentDao.findById(1).isPresent());

        mockMvc.perform(delete("/student/{id}",1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        assertFalse(studentDao.findById(1).isPresent());


    }

    @Test
    void deleteStudentHttpRequestError() throws Exception {

        assertFalse(studentDao.findById(0).isPresent());

        mockMvc.perform(delete("/student/{id}", 0))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status", Matchers.is(404)))
                .andExpect(jsonPath("$.message", Matchers.is("Student or Grade was not found")));

    }

    @Test
    void getStudentHttpRequestStudentInformation() throws Exception {

        assertTrue(studentDao.findById(1).isPresent());

        mockMvc.perform(get("/studentInformation/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname", Matchers.is("Eric")))
                .andExpect(jsonPath("$.lastname", Matchers.is("Roby")))
                .andExpect(jsonPath("$.emailAddress", Matchers.is("eric.roby@luv2code_school.com")));

    }

    @Test
    void getStudentHttpRequestStudentInformationError() throws Exception {

        assertFalse(studentDao.findById(0).isPresent());

        mockMvc.perform(get("/studentInformation/{id}", 0))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status", Matchers.is(404)))
                .andExpect(jsonPath("$.message", Matchers.is("Student or Grade was not found")));

    }

    @Test
    public void createGradeHttpRequest() throws Exception {

        mockMvc.perform(post("/grades")
                        .param("grade", String.valueOf(1.0))
                        .param("gradeType", "math")
                        .param("studentId", String.valueOf(1)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.studentGrades.mathGradeResults", hasSize(2)));


    }

    @Test
    void createGradeHttpRequestStudentError() throws Exception {

        assertFalse(studentDao.findById(0).isPresent());

        mockMvc.perform(post("/grades")
                        .param("grade", String.valueOf(1.0))
                        .param("gradeType", "math")
                        .param("studentId", String.valueOf(0)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status", Matchers.is(404)))
                .andExpect(jsonPath("$.message", Matchers.is("Student or Grade was not found")));

    }

    @Test
    void createGradeHttpRequestGradeError() throws Exception {

        assertFalse(studentService.createGrade(1.0,1, "Outra cena"));

        mockMvc.perform(post("/grades")
                        .param("grade", String.valueOf(1.0))
                        .param("gradeType", "Outra cena")
                        .param("studentId", String.valueOf(1)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status", Matchers.is(404)))
                .andExpect(jsonPath("$.message", Matchers.is("Student or Grade was not found")));

    }

    @Test
    void deleteGradeHttpRequestGrade() throws Exception {

        assertTrue(mathGradeDao.findById(1).isPresent());

        mockMvc.perform(delete("/grades/{id}/{gradeType}",1, "math"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.studentGrades.mathGradeResults", hasSize(0)));

    }

    @Test
    void deleteGradeHttpRequestGradeIdError() throws Exception {

        assertFalse(mathGradeDao.findById(-2000).isPresent());

        mockMvc.perform(delete("/grades/{id}/{gradeType}",-2000, "math"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status", Matchers.is(404)))
                .andExpect(jsonPath("$.message", Matchers.is("Student or Grade was not found")));

    }

    @Test
    void deleteGradeHttpRequestGradeSubjectNameError() throws Exception {

        assertFalse(studentService.createGrade(1.0,1, "OutraCena"));

        mockMvc.perform(delete("/grades/{id}/{gradeType}",-2000, "OutraCena"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.status", Matchers.is(404)))
                .andExpect(jsonPath("$.message", Matchers.is("Student or Grade was not found")));

    }


    @AfterEach
    public void setupAfterTransaction() {
        jdbc.execute(sqlDeleteStudent);
        jdbc.execute(sqlDeleteMathGrade);
        jdbc.execute(sqlDeleteScienceGrade);
        jdbc.execute(sqlDeleteHistoryGrade);
    }

}








