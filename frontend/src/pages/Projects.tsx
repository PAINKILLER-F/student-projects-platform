import React from 'react';
import {Container, makeStyles, Paper, Typography} from "@material-ui/core";
import Centered from "../components/util/Centered";
import DefaultBadge from "../components/elements/DefaultBadge";
import ProjectMenu from "../components/elements/ProjectMenu";
import QueryPanel from "../components/elements/QueryPanel";
import {useParams} from "react-router-dom";
import PagingPanel from "../components/elements/PagingPanel";
import BadgePage from "../components/elements/BadgePage";


const useStyles = makeStyles(theme => ({
    main: {
        display: "flex",
        flexWrap: "wrap",
        flexDirection: "row",
        alignItems: "start",
    },
    query: {
        // margin: '2% 0 0 0'
        padding: '10px'
    },
    title: {
        margin: '45px 0 10px 0'
    }
}));

interface ProjectsParams {
    workspaceId: string,
    workspaceTitle: string
}

export default function Projects() {
    const classes = useStyles();

    const {workspaceId, workspaceTitle} = useParams<ProjectsParams>();

    console.log("render Projects")

    // return (
    //     <>
    //         <Typography className={classes.title} variant='h3'>Проекты из {workspaceTitle}</Typography>
    //         <ProjectMenu/>
    //         <QueryPanel/>
    //         <Container>
    //             <Centered row={true} additionalClasses={[classes.main]}>
    //                 {['A', 'B', 'C', 'D', 'E', 'F', 'A1', '1B', 'C1', 'D1', 'E1', '1F'].map(s =>
    //                     <DefaultBadge key={s} id={s} title={s}/>)}
    //             </Centered>
    //         </Container>
    //         <PagingPanel />
    //     </>);

    return (
        <>
            <ProjectMenu />
            <BadgePage title={`Проекты из ${workspaceTitle}`}
                       badgeData={['A', 'B', 'C', 'D', 'E', 'F', 'A1', '1B', 'C1', 'D1', 'E1', '1F'].map(s => ({id: s}))} />
        </>);
}