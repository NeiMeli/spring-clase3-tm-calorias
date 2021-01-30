package com.bootcamp.calculadoradecalorias.dao.impl.json;

import com.bootcamp.calculadoradecalorias.dao.CaloriesDAO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CaloriesJsonDAOTest {
    @Autowired
    CaloriesDAO dao;

    @Test
    void testFindCaseInsensitive() {
        double c1 = dao.getCaloriesPerHundredGrams("AcElgAs");
        assertNotEquals(c1, CaloriesDAO.NOT_FOUND);
        double c2 = dao.getCaloriesPerHundredGrams("acelgas");
        assertNotEquals(c2, CaloriesDAO.NOT_FOUND);
    }

    @Test
    void testNotFound() {
        double c1 = dao.getCaloriesPerHundredGrams("not-found");
        assertEquals(c1, CaloriesDAO.NOT_FOUND);
        double c2 = dao.getCaloriesPerHundredGrams("");
        assertEquals(c2, CaloriesDAO.NOT_FOUND);
    }
}