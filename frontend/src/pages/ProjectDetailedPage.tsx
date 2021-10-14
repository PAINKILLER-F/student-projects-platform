import React, {useEffect, useState} from 'react';
import {useParams} from "react-router-dom";
import {shallowEqual, useDispatch, useSelector} from "react-redux";
import {getProjectInfo, getProjectsForWorkspace} from "../api/projects";
import Project, {DetailedProject} from "../model/Project";
import getPaging from "../hooks/getPaging";
import Typography from "@material-ui/core/Typography";
import {Button, ListItemText, ListSubheader, makeStyles, Paper} from "@material-ui/core";
import List from "@material-ui/core/List";
import {ListItemButton} from "@mui/material";
import TagsPanel from "../components/util/TagsPanel";
// import Divider from "@material-ui/core/Divider";
import Centered from "../components/util/Centered";
import { Divider, CssBaseline } from '@mui/material';

const useStyles = makeStyles(theme => ({
    paper: {
        padding: '10px 20px',
        width: '70vw',
        height: '70vh',
    },
    inner: {
        width: '100%',
        // height: '100%',
    },
    descr: {
        margin: '30px 15px'
    },
    butGr: {
        margin: '10px',
        display: 'flex',
        flexDirection: 'row',
        justifyContent: 'end',
        width: '100%',
    }
}));

interface ProjectParams {
    workspaceId: string,
    projectId: string
}

export default function ProjectDetailedPage() {
    const {workspaceId, projectId} = useParams<ProjectParams>();
    const classes = useStyles();
    const [project, setProject] = useState(null as DetailedProject | null);

    useEffect(() => {
            getProjectInfo(workspaceId, projectId).then(r => setProject(r.payload))
        }, //TODO: catch
        [workspaceId, projectId]);
    console.log('render with')
    console.log(project)
    return (
        <Paper className={classes.paper}>
            <Centered additionalClasses={[classes.inner]}>
                <Typography variant='h4'>Проекта {project?.title}</Typography>
                <TagsPanel onSetTag={() => {}} editable={false} values={project?.tags}/>
                <Divider flexItem/>
                <Typography className={classes.descr}>{project?.description}</Typography>
                <Divider flexItem/>
                <List>
                    <List
                        // sx={{ width: '100%', maxWidth: 360, bgcolor: 'background.paper' }}
                        component="nav"
                        aria-labelledby="nested-list-subheader"
                        subheader={
                            <ListSubheader component="div" id="nested-list-subheader">
                                Список участников
                            </ListSubheader>
                        }>
                        {project?.participantLogins.map(p => (
                            <ListItemButton key={p}>
                                <ListItemText primary={p}/>
                            </ListItemButton>))}
                    </List>
                </List>
                <div className={classes.butGr}>
                    <Button>Присоединиться</Button>
                    <Button href={`/project_plan?projectId=${projectId}&workspaceId=${workspaceId}`}>Просмотр плана</Button>
                </div>
            </Centered>
        </Paper>
    );
}
