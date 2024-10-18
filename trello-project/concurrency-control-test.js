import http from "k6/http"
import {htmlReport} from "https://raw.githubusercontent.com/benc-uk/k6-reporter/main/dist/bundle.js"

export const options = {
    vus: 100, // 가상의 유저수
    duration: "1s",
}

export default function () {
    const url = `http://localhost:8080/workspaces/1/lists/1/cards/1`;

    const payload = JSON.stringify({
        title: "제목",
        description: "설명",
        deadline: "2024-10-17",
        status: "DONE",
        managers: []
    });

    const params = {
        headers: {
            'Content-Type': "application/json",
            'Authorization': "Bearer eyJhbGciOiJIUzM4NCJ9.eyJjYXRlZ29yeSI6IkFDQ0VTUyIsImV4cCI6MTcyOTE2MzMwNSwic3ViIjoiMiIsImVtYWlsIjoiYkBhLmNvbSIsInVzZXJSb2xlIjoiUk9MRV9VU0VSIiwiaWF0IjoxNzI5MTQ4OTA1fQ.aEXRt5x-lp8jMclbte8s50sJf-_t21lZ3i8Z48czumJmTbFMdHXsGUhYMZoaB-he"
        }
    }
    http.patch(url, payload, params)
}

export function handleSummary(data) {
    return {
        "enable-concurrency-control-summary.html": htmlReport(data)
    }
}