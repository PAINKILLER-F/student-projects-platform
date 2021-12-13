package com.platform.projapp.controller;

import com.platform.projapp.dto.request.ScoreRequest;
import com.platform.projapp.dto.response.GeneralResponse;
import com.platform.projapp.dto.response.body.ProjectWithScoresResponseBody;
import com.platform.projapp.dto.response.body.ProjectsWithScoresResponseBody;
import com.platform.projapp.dto.response.body.ScoreResponseBody;
import com.platform.projapp.dto.response.body.ScoresResponseBody;
import com.platform.projapp.error.ErrorConstants;
import com.platform.projapp.service.ScoreService;
import com.platform.projapp.service.SprintsService;
import com.platform.projapp.service.UserService;
import com.platform.projapp.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Yarullin Renat
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/scores")
public class ScoreController {
    private final ScoreService scoreService;
    private final SprintsService sprintsService;
    private final WorkspaceService workspaceService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> getScores(@RequestParam(name = "workspaceId") Long workspaceId) {
        var workspace = workspaceService.findById(workspaceId);
        ResponseEntity<?> workspaceErrorResponseEntity = workspaceService.getWorkspaceErrorResponseEntity(workspace,
                "",
                Collections.emptyList());
        if (workspaceErrorResponseEntity != null)
            return workspaceErrorResponseEntity;
        Set<ProjectWithScoresResponseBody> projectWithScoresResponseBodies = workspace.getProjects().stream()
                .map(ProjectWithScoresResponseBody::fromProject)
                .collect(Collectors.toSet());
        Integer sprintsCount = projectWithScoresResponseBodies.stream()
                .map(p -> p.getScores().size())
                .reduce(0, Math::max);
        return ResponseEntity.ok(new GeneralResponse<>().withData(ProjectsWithScoresResponseBody.of(sprintsCount, projectWithScoresResponseBodies)));
    }

    @GetMapping("/evaluate")
    public ResponseEntity<?> getTableOfScores(@RequestHeader(name = "Authorization") String token,
                                              @RequestParam(name = "workspaceId") Long workspaceId,
                                              @RequestParam(name = "sprintNumber") Integer sprintNumber) {
        var user = userService.parseAndFindByJwt(token);
        var workspace = workspaceService.findById(workspaceId);
                ResponseEntity < ?>
        workspaceErrorResponseEntity = workspaceService.getWorkspaceErrorResponseEntity(workspace,
                user.getLogin(),
                List.of(ErrorConstants.USER_NOT_WORKSPACE_MENTOR_OR_OWNER));
        if (workspaceErrorResponseEntity != null)
            return workspaceErrorResponseEntity;
        Integer currentSprintOrderNumber;
        if (sprintNumber == -1)
            currentSprintOrderNumber = sprintsService.getCurrentSprintOrderNumberByWorkspaceAndDate(workspace, LocalDate.now());
        else
            currentSprintOrderNumber = sprintNumber;
        var sprints = sprintsService.findAllByWorkspaceAndOrderNumber(workspace, currentSprintOrderNumber);
        Set<ScoreResponseBody> scoreResponseBodies = sprints.stream().map(s -> ScoreResponseBody.fromSprint(s, user)).collect(Collectors.toSet());
        return ResponseEntity.ok(new GeneralResponse<>().withData(ScoresResponseBody.of(currentSprintOrderNumber,scoreResponseBodies)));
    }

    @PostMapping("/evaluate")
    public ResponseEntity<?> addScores(@RequestHeader(name = "Authorization") String token,
                                       @RequestBody List<ScoreRequest> scoreRequestList) {
        var user = userService.parseAndFindByJwt(token);
        scoreService.addOrUpdateScores(scoreRequestList, user);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}