import { EVENT_ID } from "./const-event"

const participantFormat = (index, sex) => ({index: index,
name: "",
sex: sex || "", 
foreigner: [],
nationality: "",
idnumber: ["", "-", ""],
weight: "",})

export const teamInitialFormat = (eventName) => {
    const event = Object.values(EVENT_ID).find(event => event.name === eventName);

    switch (eventName) {
        case "겨루기 여성":
            return ({
                event: event.id,
                teamMembers: [
                  participantFormat("1번 선수", "여성"),
                  participantFormat("2번 선수", "여성"),
                  participantFormat("3번 선수", "여성"),
                  participantFormat("후보 선수", "여성"),
                ],
            })
        case "품새 여성":
            return {
                event: event.id,
                teamMembers: [
                    participantFormat("1번 선수", "여성"),
                    participantFormat("2번 선수", "여성"),
                    participantFormat("3번 선수", "여성"),
                ],
            }
        case "겨루기 남성":
            return {
                event: event.id,
                teamMembers: [
                    participantFormat("1번 선수", "남성"),
                    participantFormat("2번 선수", "남성"),
                    participantFormat("3번 선수", "남성"),
                    participantFormat("후보 선수", "남성"),
                ],
            }
        case "품새 남성":
            return {
                event: event.id,
                teamMembers: [
                    participantFormat("1번 선수", "남성"),
                    participantFormat("2번 선수", "남성"),
                    participantFormat("3번 선수", "남성"),
                ],
            }
        case "품새 페어":
            return {
                event: event.id,
                teamMembers: [
                    participantFormat("1번 선수", "남성"),
                    participantFormat("2번 선수", "여성"),
                ],
            }
    }
    
}