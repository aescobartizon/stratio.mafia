package com.stratio.anescobar.mafia.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stratio.anescobar.mafia.components.AbstractGenericService;
import com.stratio.anescobar.mafia.components.MafiaHierarchyFatory;
import com.stratio.anescobar.mafia.domain.Mafioso;
import com.stratio.anescobar.mafia.domain.ReportingBody;
import com.stratio.anescobar.mafia.domain.UpdateBossStatus;

import io.swagger.annotations.ApiOperation;
import lombok.Getter;

@RestController
public class MafiaApi extends AbstractGenericService implements IMafiaApi {

	@Autowired
	@Getter
	private MafiaHierarchyFatory mafiaHierarchyFatory;

	private static final String MAFIA_END_POINT_V1 = "/api/v1";

	@PostMapping(path = MAFIA_END_POINT_V1 + "/reporting")
	@ApiOperation(value = "Add new reporting in the system", response = String.class)
	public ResponseEntity<String> save(@RequestBody ReportingBody reportingBody) {

		getMafiaHierarchyFatory().getService().reporting(reportingBody.getFullNameReporter(), reportingBody.getFullNameBoss());

		return new ResponseEntity<>("Reporting adding to system", HttpStatus.CREATED);

	}

	@GetMapping(path = MAFIA_END_POINT_V1 + "/activebosses")
	@ApiOperation(value = "Get the active Bosses in the system", response = ReportingBody.class)
	public ResponseEntity<List<Mafioso>> getActiveBosses() {

		return new ResponseEntity<>(getMafiaHierarchyFatory().getService().getActiveBosses(), HttpStatus.OK);

	}

	@GetMapping(path = MAFIA_END_POINT_V1 + "/reporters")
	@ApiOperation(value = "Get the boss reporters ", response = ReportingBody.class)
	public ResponseEntity<List<Mafioso>> getReporters(@RequestParam String fullNameBoss) {

		return new ResponseEntity<>(getMafiaHierarchyFatory().getService().getReportingByName(fullNameBoss), HttpStatus.OK);

	}

	@PutMapping(path = MAFIA_END_POINT_V1 + "/update/status")
	@ApiOperation(value = "Update boss Status [ FREE, ENCARCELADO, ASSESINADO ]", response = String.class)
	public ResponseEntity<String> updateStatusBoss(@RequestBody UpdateBossStatus updateBossStatus) {

		getMafiaHierarchyFatory().getService().updateStatusMafioso(updateBossStatus.getFullNameBoss(), updateBossStatus.getMafiosoStatus());

		return new ResponseEntity<>("Update boss status ", HttpStatus.ACCEPTED);

	}

}
