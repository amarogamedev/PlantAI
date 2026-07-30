import React, { useState, useRef } from 'react';
import {streamChat} from "./services/api.ts";

export default function App() {
    const [prompt, setPrompt] = useState('');
    const [response, setResponse] = useState('');
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    const abortControllerRef = useRef<AbortController | null>(null);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!prompt.trim() || loading) return;

        setLoading(true);
        setResponse('');
        setError(null);

        const controller = new AbortController();
        abortControllerRef.current = controller;

        try {
            await streamChat({
                prompt,
                signal: controller.signal,
                onChunk: (chunk) => {
                    setResponse((prev) => prev + chunk);
                },
            });
        } catch (err: any) {
            if (err.name === 'AbortError') {
                console.log('Streaming interrupted by the user.');
            } else {
                console.error('Request error:', err);
                setError(err.message || 'An error occurred.');
            }
        } finally {
            setLoading(false);
            abortControllerRef.current = null;
        }
    };

    const handleStop = () => {
        abortControllerRef.current?.abort();
    };

    return (
        <div style={styles.container}>
            <h2>Chat Streaming (Spring WebFlux + React)</h2>

            <form onSubmit={handleSubmit} style={styles.form}>
        <textarea
            value={prompt}
            onChange={(e) => setPrompt(e.target.value)}
            placeholder="How can I help you?"
            rows={4}
            style={styles.textarea}
            disabled={loading}
        />

                <div style={styles.buttonGroup}>
                    {!loading ? (
                        <button type="submit" style={styles.buttonSubmit} disabled={!prompt.trim()}>
                            Send
                        </button>
                    ) : (
                        <button type="button" onClick={handleStop} style={styles.buttonStop}>
                            Stop response
                        </button>
                    )}
                </div>
            </form>

            {error && <div style={styles.errorBox}>{error}</div>}

            <div style={styles.responseBox}>
                <strong style={{ display: 'block', marginBottom: '8px' }}>Response:</strong>
                <div style={styles.responseText}>
                    {response || (loading ? 'Waiting response...' : 'The response will show up here.')}
                </div>
            </div>
        </div>
    );
}

const styles: { [key: string]: React.CSSProperties } = {
    container: {
        maxWidth: '650px',
        margin: '40px auto',
        padding: '20px',
        fontFamily: 'system-ui, -apple-system, sans-serif',
    },
    form: {
        display: 'flex',
        flexDirection: 'column',
        gap: '12px',
    },
    textarea: {
        width: '100%',
        padding: '12px',
        borderRadius: '8px',
        border: '1px solid #ccc',
        fontSize: '15px',
        resize: 'vertical',
        boxSizing: 'border-box',
    },
    buttonGroup: {
        display: 'flex',
        justifyContent: 'flex-end',
    },
    buttonSubmit: {
        padding: '10px 20px',
        borderRadius: '6px',
        border: 'none',
        backgroundColor: '#0066cc',
        color: '#fff',
        fontSize: '14px',
        cursor: 'pointer',
    },
    buttonStop: {
        padding: '10px 20px',
        borderRadius: '6px',
        border: 'none',
        backgroundColor: '#dc3545',
        color: '#fff',
        fontSize: '14px',
        cursor: 'pointer',
    },
    errorBox: {
        marginTop: '16px',
        padding: '12px',
        backgroundColor: '#f8d7da',
        color: '#721c24',
        borderRadius: '6px',
    },
    responseBox: {
        marginTop: '24px',
        padding: '16px',
        backgroundColor: '#f4f4f6',
        borderRadius: '8px',
        minHeight: '100px',
    },
    responseText: {
        whiteSpace: 'pre-wrap',
        wordBreak: 'break-word',
        fontSize: '15px',
        lineHeight: '1.5',
    },
};