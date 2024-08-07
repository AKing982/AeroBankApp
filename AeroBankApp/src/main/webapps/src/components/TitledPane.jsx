import {Component} from "react";

class TitledPane extends Component{
    constructor(props)
    {
        super(props);
        this.state = {
            isExpanded: true,
        };
    }

    toggleAccordion = () => {
        this.setState((prevState) => ({
            isExpanded: !prevState.isExpanded,
        }));
    };

    render() {
        const { title, children } = this.props;
        const { isExpanded } = this.state;

        return (
            <div className="titled-pane">
                <div className={`titled-pane-header ${isExpanded ? 'expanded' : ''}`} onClick={this.toggleAccordion}>
                    {title}
                </div>
                <div className={`titled-pane-content ${isExpanded ? 'expanded' : ''}`}>{isExpanded ? children : null}</div>
            </div>
        );
    }
}

export default TitledPane;



