const API_URL = 'http://localhost:8080/api/chat';

export interface ChatPromptDTO {
    prompt: string;
}

export interface StreamChatOptions {
    prompt: string;
    onChunk: (chunk: string) => void;
    signal?: AbortSignal;
}

export async function streamChat({ prompt, onChunk, signal }: StreamChatOptions): Promise<void> {
    const response = await fetch(API_URL, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'text/event-stream',
        },
        body: JSON.stringify({ prompt } as ChatPromptDTO),
        signal,
    });

    if (!response.ok) {
        throw new Error(`API error (${response.status}): ${response.statusText}`);
    }

    if (!response.body) {
        throw new Error('Empty response.');
    }

    const reader = response.body.getReader();
    const decoder = new TextDecoder('utf-8');
    let buffer = '';

    while (true) {
        const { value, done } = await reader.read();
        if (done) break;

        buffer += decoder.decode(value, { stream: true });
        const lines = buffer.split('\n');
        buffer = lines.pop() ?? '';

        for (const line of lines) {
            const cleanLine = line.replace(/\r$/, '');
            if (cleanLine.startsWith('data:')) {
                const chunk = cleanLine.replace(/^data:/, '');
                if (chunk) {
                    onChunk(chunk);
                }
            }
        }
    }

    if (buffer) {
        const cleanLine = buffer.replace(/\r$/, '');
        if (cleanLine.startsWith('data:')) {
            const chunk = cleanLine.replace(/^data:/, '');
            if (chunk) onChunk(chunk);
        }
    }
}