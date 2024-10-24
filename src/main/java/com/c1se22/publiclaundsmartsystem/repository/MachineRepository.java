package com.c1se22.publiclaundsmartsystem.repository;

import com.c1se22.publiclaundsmartsystem.entity.Machine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MachineRepository extends JpaRepository<Machine, Integer> {
    @Modifying
    @Query(value = "update Machine as m set m.location.id = :locationId where m.id in :machineIds")
    int updateLocationOfMachines(@Param("locationId") Integer locationId, @Param("machineIds") List<Integer> machineIds);
    @Query(value = "select distinct m from Machine m join UsageHistory u on m.id = u.machine.id where m.status = 'IN_USE' and u.user.id = :userId and u.endTime is null group by m.id")
    List<Machine> findMachinesAreBeingUsedByUser(@Param("userId") Integer userId);
}
