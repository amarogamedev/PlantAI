import React from 'react';
import {PaperPlaneRightIcon, XIcon} from "@phosphor-icons/react";

interface ChatInputProps {
    prompt: string;
    setPrompt: (value: string) => void;
    loading: boolean;
    onSubmit: (e: React.FormEvent) => void;
    onStop: () => void;
}

export const ChatInput: React.FC<ChatInputProps> = ({prompt, setPrompt, loading, onSubmit, onStop}) => {
    const handleKeyDown = (e: React.KeyboardEvent<HTMLTextAreaElement>) => {
        if (e.key === 'Enter' && !e.shiftKey) {
            e.preventDefault();
            if (prompt.trim() && !loading) {
                onSubmit(e);
            }
        }
    };

    return (
        <form onSubmit={onSubmit} style={styles.formContainer}>
            <textarea
                value={prompt}
                onChange={(e) => setPrompt(e.target.value)}
                onKeyDown={handleKeyDown}
                placeholder="Type a message... (Press enter to send)"
                rows={3}
                style={styles.textarea}
                disabled={loading}
            />
            <div style={styles.buttonWrapper}>
                {!loading ? (
                    <button
                        type="submit"
                        disabled={!prompt.trim()}
                        style={{
                        ...styles.button,
                        ...styles.submitButton,
                        opacity: !prompt.trim() ? 0.5 : 1,
                        cursor: !prompt.trim() ? 'not-allowed' : 'pointer',
                        }}
                    >
                    <PaperPlaneRightIcon size={24} color={'#101010'} weight="fill"/>
                    </button>
                ) : (
                    <button type="button" onClick={onStop} style={{...styles.button, ...styles.stopButton}}>
                    <XIcon size={24} color={'#101010'} weight="fill"/>
                    </button>
                )}
            </div>
        </form>
    );
};

const styles: { [key: string]: React.CSSProperties } = {
    formContainer: {
        display: 'flex',
        gap: '12px',
        alignItems: 'center',
        backgroundColor: '#101010',
        padding: '12px',
        borderRadius: '12px',
        border: '1px solid #404040',
    },
    textarea: {
        flex: 1,
        backgroundColor: 'transparent',
        border: 'none',
        outline: 'none',
        color: '#f8fafc',
        fontSize: '15px',
        resize: 'none',
        fontFamily: 'inherit',
    },
    buttonWrapper: {
        display: 'flex',
        alignItems: 'center',
    },
    button: {
        padding: '16px',
        borderRadius: '8px',
        border: 'none',
        fontWeight: 600,
        fontSize: '14px',
        transition: 'background-color 0.2s ease',
    },
    submitButton: {
        backgroundColor: '#7cc668',
        color: '#101010',
        borderRadius: '64px',
    },
    stopButton: {
        backgroundColor: '#fca5a5',
        color: '#101010',
        cursor: 'pointer',
        borderRadius: '64px',
    },
};