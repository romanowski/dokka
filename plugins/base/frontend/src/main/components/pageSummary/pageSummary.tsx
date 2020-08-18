import React, { useState, useEffect } from "react";
import './pageSummary.scss'
import Arrow from 'react-svg-loader!../assets/arrow.svg';
import _ from "lodash";

type PageSummaryProps = {
    category: string,
    entries: PageSummaryEntry[]
}

type PageSummaryEntry = {
    location: string,
    label: string,
    htmlElement: HTMLElement
}

export const PageSummary: React.FC<PageSummaryProps> = ({category, entries}: PageSummaryProps) => {
    const [hidden, setHidden] = useState<Boolean>(false);
    const [highlighted, setHighlighted] = useState<PageSummaryEntry | null>(null);

    const onScroll = () => {
        const currentPosition = document.documentElement.scrollTop || document.body.scrollTop;
        const closest = _.minBy(entries, (element: PageSummaryEntry) => Math.abs(currentPosition - element.htmlElement.offsetTop))
        setHighlighted(closest)
    }

    useEffect(() => {
        window.addEventListener('scroll', onScroll)
        return () => window.removeEventListener('scroll', onScroll)
    })

    const toggleHide = () => setHidden(!hidden)

    let classnames = "page-summary " + category
    if(hidden) classnames += " hidden"
    
    return (
        <div className={classnames}>
            <span className={"clickable-icon"} onClick={() => toggleHide()}><Arrow /></span>
            {!hidden && 
                <div className={"content-wrapper"}>
                    <h4>On this page:</h4>
                    <ul>
                        {entries.map((item) => <SummaryElement location={item.location} label={item.label} highlighted={highlighted === item}/>)}
                    </ul>
                </div>}
        </div>
    )
}

type PageSummaryEntryProps = {
    location: string,
    label: string,
    highlighted: boolean;
}

const SummaryElement: React.FC<PageSummaryEntryProps> = ({location, label, highlighted}: PageSummaryEntryProps) => {
    let className = highlighted ? 'selected' : ''
    return (
        <li className={className}><a href={`#${location}`}>{label}</a></li>
    )
}