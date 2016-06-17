package org.openlmis.referencedata.repository;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import org.openlmis.referencedata.domain.Period;
import org.openlmis.referencedata.domain.Schedule;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.UUID;

public class PeriodRepositoryIntegrationTest extends BaseCrudRepositoryIntegrationTest<Period>{

    @Autowired
    PeriodRepository periodRepository;

    @Autowired
    ScheduleRepository scheduleRepository;

    PeriodRepository getRepository() {
        return this.periodRepository;
    }

    private Schedule schedule = new Schedule();

    @Before
    public void setUp() {
        periodRepository.deleteAll();
        scheduleRepository.deleteAll();
        schedule.setCode("code");
        schedule.setName("schedule");
        schedule.setDescription("Test schedule");
        scheduleRepository.save(schedule);
    }

    Period generateInstance() {
        int instanceNumber = this.getNextInstanceNumber();
        Period period = new Period();
        period.setName("period" + instanceNumber);
        period.setProcessingSchedule(schedule);
        period.setDescription("Test period");
        period.setStartDate(LocalDate.of(2016, 1, 1));
        period.setEndDate(LocalDate.of(2016, 2, 1));
        return period;
    }

    //@Ignore
    @Test
    public void testGetAllPeriods() {
        periodRepository.save(generateInstance());
        Iterable<Period> result = periodRepository.findAll();
        Assert.assertEquals(1, countSizeOfIterable(result));
    }

    @Test
    public void testPeriodEdit() {
        Period periodFromRepo = this.generateInstance();
        periodFromRepo = periodRepository.save(periodFromRepo);
        UUID id = periodFromRepo.getId();
        periodFromRepo = periodRepository.findOne(id);
        String description = "New test description";
        Assert.assertNotEquals(description, periodFromRepo.getDescription());
        periodFromRepo.setDescription(description);
        periodFromRepo.setStartDate(LocalDate.of(2016, 2, 2));
        periodFromRepo.setEndDate(LocalDate.of(2016, 3, 2));
        periodRepository.save(periodFromRepo);
        Assert.assertEquals(description, periodFromRepo.getDescription());
    }

    private int countSizeOfIterable(Iterable<Period> iterable) {
        int size = 0;
        for(Period p : iterable) size++;
        return size;
    }

}