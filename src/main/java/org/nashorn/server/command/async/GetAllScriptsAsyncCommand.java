package org.nashorn.server.command.async;

import org.nashorn.server.CommandExecutionException;
import org.nashorn.server.HttpRequestEntity;
import org.nashorn.server.HttpResponsePublisher;
import org.nashorn.server.command.AbstractCommand;
import org.nashorn.server.core.ExecutionUnit;
import org.nashorn.server.db.InMemoryStorage;
import org.nashorn.server.util.response.ScriptExecutionUnitData;
import org.nashorn.server.util.response.UriBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

public class GetAllScriptsAsyncCommand extends AbstractCommand {

    @Override
    public void execute(HttpRequestEntity request, HttpResponsePublisher pub)
            throws CommandExecutionException, ServletException {

        Set<ConcurrentMap.Entry<Long, ExecutionUnit>> entrySet = InMemoryStorage.instance().getAllUnits();

        if (!entrySet.isEmpty()) {
            pub.statusOK();
            pub.content(buildListForResponse(entrySet, request));
        } else {
            pub.statusNoContent();
        }
        pub.publish();
    }

    private List<ScriptExecutionUnitData> buildListForResponse(Set<ConcurrentMap.Entry<Long, ExecutionUnit>> entrySet,
                                                               HttpServletRequest req) {
        List<ScriptExecutionUnitData> units = new ArrayList<>();
        for (ConcurrentMap.Entry<Long, ExecutionUnit> entry : entrySet) {
            ScriptExecutionUnitData unitData = new ScriptExecutionUnitData();
            unitData.setId(entry.getKey());
            unitData.setUnit(entry.getValue());
            unitData.setLocation(new UriBuilder(req).append(entry.getKey()).build());
            units.add(unitData);
        }
        return units;
    }
}
