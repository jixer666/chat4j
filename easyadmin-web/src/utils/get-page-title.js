import defaultSettings from '@/settings'

const title = defaultSettings.title || 'Chat4j'

export default function getPageTitle(pageTitle) {
  if (pageTitle) {
    return `${pageTitle} - ${title}`
  }
  return `${title}`
}
