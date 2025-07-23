import { Pollutant } from 'src/app/shared/models/pollutant';

export function formatPollutant(result: Pollutant): string {
  const formattedResult = `${result.pollutantName}  -  ${result.pollutantCode}`
  return result.pollutantCasId ? formattedResult + `  -  ${result.pollutantCasId}` : formattedResult;
}
