import VerticalTabs from "./VerticalTab";
import Home from "./Home";

export default function SettingsView()
{
    return (
        <div>
            <Home />
            <div className="settings-view-container">
                <header className="settings-view-header">
                </header>
                <div className="settings-tabs">
                    <VerticalTabs />
                </div>
            </div>
        </div>

    );
}