import VerticalTabs from "./VerticalTab";
import Home from "./Home";
import GradientSeparator from "./GradientSeparator";
import MenuAppBar from "./MenuAppBar";

export default function SettingsView()
{
    return (
        <div>
            <MenuAppBar />
            <GradientSeparator />
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