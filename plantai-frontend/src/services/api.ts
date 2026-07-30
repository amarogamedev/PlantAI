const API_URL = 'http://localhost:8080/api/chat';

export interface ChatPromptDTO {
    prompt: string;
}

export interface SendChatOptions {
    prompt: string;
    signal?: AbortSignal;
}

export async function sendChat({ prompt, signal }: SendChatOptions): Promise<string> {
    const response = await fetch(API_URL, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'text/plain',
        },
        body: JSON.stringify({ prompt } as ChatPromptDTO),
        signal,
    });

    if (!response.ok) {
        throw new Error(`API error (${response.status}): ${response.statusText}`);
    }

    return await response.text();
}