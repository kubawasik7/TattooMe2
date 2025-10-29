package TattooMe.TattooMe.repository;

import TattooMe.TattooMe.entity.WorkHour;
import TattooMe.TattooMe.entity.WorkHourStudio;
import TattooMe.TattooMe.entity.WorkHourStudioId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WorkHourStudioRepository extends JpaRepository<WorkHourStudio, WorkHourStudioId> {

    @Query("select whs.workHour from WorkHourStudio whs where whs.id.tattooStudioId = :studioId order by " +
            "CASE " +
            "WHEN whs.workHour.dayOfWeek = 'MONDAY' THEN 1 WHEN whs.workHour.dayOfWeek = 'TUESDAY' THEN 2 " +
            "WHEN whs.workHour.dayOfWeek = 'WEDNESDAY' THEN 3 WHEN whs.workHour.dayOfWeek = 'THURSDAY' THEN 4 " +
            "WHEN whs.workHour.dayOfWeek = 'FRIDAY' THEN 5 WHEN whs.workHour.dayOfWeek = 'SATURDAY' THEN 6 " +
            "WHEN whs.workHour.dayOfWeek = 'SUNDAY' THEN 7 ELSE 99 END, whs.workHour.startTime")
    List<WorkHour> findAllWorkHoursByStudioId(@Param("studioId") UUID studioId);
}
