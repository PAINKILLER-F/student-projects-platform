export default class Project {
    constructor(public id: string, public workSpaceId: string, public title: string,
                public description: string, public participantLogins: string[], public tags: string[]) {
    }
}